package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.VacationRequestRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class VacationController{

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests( HttpServletRequest request,
                                                      @RequestParam("date") Optional<String> date,
                                                      @RequestParam("page") Optional<Integer> page,
                                                      @RequestParam("amount") Optional<Integer> amount){
        /*
        * TODO - Date searches
        * [ ] - Add date search
        * [ } - Add between two dates search
        */
        if (!authorizationService.isAuthorized(request)) { return unauthorized(); }
        final List<VacationRequest> allVacationRequests = vacationRequestRepository
                .findAllByOrderByStartDesc(PageRequest.of(page.orElse(0), amount.orElse(50)));
        if (authorizationService.currentUser(request).isAdmin())
            return ResponseEntity.ok(new CommonResponse(
                    "All vacation requests", allVacationRequests));
        return ResponseEntity.ok(new CommonResponse(
                "All vacation requests",
                allVacationRequests.stream().filter((vr) ->
                        filterApprovedAndOwnerRequests(vr, request))
                .collect(Collectors.toList())));
    }

    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        if (!authorizationService.isAuthorized(request)) { return unauthorized(); }
        final User currentUser = authorizationService.currentUser(request);
        final List<VacationRequest> vacationRequests = vacationRequestRepository.findAllByOwner(currentUser);
        if (vacationRequests.stream().noneMatch(vacationRequest::excludesInPeriod)) {
            try {
                vacationRequestRepository.save(vacationRequest.setStatus(StatusType.PENDING.getStatus()));
                return ResponseEntity.accepted().body(new CommonResponse("Added"));
            } catch (Exception e) { return errorMessage(); }
        } else return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse("Overlaps with existing requests"));
    }

    @GetMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id") Long requestId,
                                                           HttpServletRequest request){
        if (!authorizationService.isAuthorized(request)) { return unauthorized(); }
        final Optional<VacationRequest> vacationRequest = vacationRequestRepository.findById(requestId);
        if (vacationRequest.isPresent()) {
            VacationRequest vr = vacationRequest.get();
            if (authorizationService.isAuthorizedAdmin(request))
                return ResponseEntity.ok(new CommonResponse("Vacation Request Found", vr));
            if (filterApprovedAndOwnerRequests(vr, request)) {
                return  ResponseEntity.ok(new CommonResponse("Vacation Request Found", vr));
            } else return forbidden();
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CommonResponse("Vacation Request Not Found"));
    }

    @PatchMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> updateRequest(@PathVariable("request_id") Long requestId,
                                                        @RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        if (!authorizationService.isAuthorized(request)) return unauthorized();
        final Optional<VacationRequest> vrOp = vacationRequestRepository.findById(requestId);
        if (vrOp.isPresent()) {
            VacationRequest vr = vrOp.get();
            final boolean isAdmin = authorizationService.isAuthorizedAdmin(request);
            final User currentUser = authorizationService.currentUser(request);
            final boolean isOwner = vr.getOriginalOwner().getId().equals(currentUser.getId());
            if (isAdmin || isOwner) {
                try {
                    if (vacationRequest.getTitle() != null) vr.setTitle(vacationRequest.getTitle());
                    if (vacationRequest.getStart() != null) vr.setStart(vacationRequest.getStart());
                    if (vacationRequest.getEnd() != null) vr.setEnd(vacationRequest.getEnd());
                    if (isAdmin && !isOwner) {
                        if (vacationRequest.getStatus() != null) vr.setStatus(vacationRequest.getStatus());
                        vr.setModerationDate(new Date(System.currentTimeMillis()));
                        vr.setModerator(currentUser);
                    } else {
                        if (vacationRequest.getStatus() != null) return forbidden();
                    }
                    vr.setModifiedAt(new Date(System.currentTimeMillis()));
                    vacationRequestRepository.save(vr);
                    return ResponseEntity.accepted().body(new CommonResponse("Updated"));
                } catch(Exception e) {return errorMessage();}
            } else return forbidden();
        } else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CommonResponse("Not found"));
    }

    @DeleteMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> deleteRequest(@PathVariable("request_id") Long requestId,
                                                        HttpServletRequest request){
        /* TODO - Is owner to delete? */
        if (authorizationService.isAuthorizedAdmin(request)) {
            if (vacationRequestRepository.existsById(requestId)) {
                vacationRequestRepository.deleteById(requestId);
                return ResponseEntity.ok(new CommonResponse("Deleted"));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponse("Vacation Request not found"));
            }
        } else {
            return unauthorized();
        }
    }

    private ResponseEntity<CommonResponse> unauthorized() {
        return new ResponseEntity<>(
                new CommonResponse( "Not Authorized"),
                HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<CommonResponse> forbidden() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new CommonResponse("Forbidden"));
    }

    private Boolean filterApprovedAndOwnerRequests(VacationRequest vr, HttpServletRequest request) {
        return  vr.onlyApproved() ||
                vr.getOriginalOwner().getId().equals(authorizationService.currentUser(request).getId());
    }

    private ResponseEntity<CommonResponse> errorMessage() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse(
                        "Something went wrong when trying to process the request "));
    }

}