package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.VacationRequestRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class VacationController{

    @Autowired private VacationRequestRepository vacationRequestRepository;

    @Autowired private AuthorizationService authService;

    @Autowired private ResponseUtility responseUtility;

    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests( HttpServletRequest request,
                                                      @RequestParam("date") Optional<String> date,
                                                      @RequestParam("page") Optional<Integer> page,
                                                      @RequestParam("amount") Optional<Integer> amount){
        /*
        * TODO - Date searches
        * [ ] - Add date search
        * [ ] - Add between two dates search
        */
        if (!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final List<VacationRequest> allVacationRequests = vacationRequestRepository
                .findAllByOrderByStartDesc(PageRequest.of(page.orElse(0), amount.orElse(50)));
        if (authService.currentUser(request).isAdmin())
            return responseUtility.ok("All vacation requests", allVacationRequests);
        return responseUtility.ok(
                "All vacation requests",
                allVacationRequests.stream().filter((vr) ->
                    filterApprovedAndOwnerRequests(vr, request))
                    .collect(Collectors.toList()));
    }

    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        if (!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final User currentUser = authService.currentUser(request);
        final List<VacationRequest> vacationRequests = vacationRequestRepository.findAllByOwner(currentUser);
        if (vacationRequests.stream().noneMatch(vacationRequest::excludesInPeriod)) {
            try {
                VacationRequest vr = vacationRequestRepository
                        .save(vacationRequest.setStatus(StatusType.PENDING.getStatus()));
                return responseUtility.created("Created", vr);
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.badRequest("Overlaps with existing requests");
    }

    @GetMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id") Long requestId,
                                                           HttpServletRequest request){
        if (!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final Optional<VacationRequest> vacationRequest = vacationRequestRepository.findById(requestId);
        if (vacationRequest.isPresent()) {
            final VacationRequest vr = vacationRequest.get();
            if (authService.isAuthorizedAdmin(request))
                return responseUtility.ok("Vacation Request Found", vr);
            if (filterApprovedAndOwnerRequests(vr, request)) {
                return responseUtility.ok("Vacation Request Found", vr);
            } else return responseUtility.forbidden();
        }
        return responseUtility.notFound("Vacation Request Not Found");
    }

    @PatchMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> updateRequest(@PathVariable("request_id") Long requestId,
                                                        @RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        if (!authService.isAuthorized(request)) return responseUtility.unauthorized();
        final Optional<VacationRequest> vrOp = vacationRequestRepository.findById(requestId);
        if (vrOp.isPresent()) {
            final VacationRequest vr = vrOp.get();
            final boolean isAdmin = authService.isAuthorizedAdmin(request);
            final User currentUser = authService.currentUser(request);
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
                    } else {if (vacationRequest.getStatus() != null) return responseUtility.forbidden(); }
                    vr.setModifiedAt(new Date(System.currentTimeMillis()));
                    vacationRequestRepository.save(vr);
                    return responseUtility.ok("Updated", null);
                } catch(Exception e) {return responseUtility.errorMessage();}
            } else return responseUtility.forbidden();
        } else return responseUtility.notFound("Vacation Request Not Found");
    }

    @DeleteMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> deleteRequest(@PathVariable("request_id") Long requestId,
                                                        HttpServletRequest request){
        if (!authService.isAuthorized(request)) return responseUtility.unauthorized();
        final User currentUser = authService.currentUser(request);
        final Optional<VacationRequest> vrOp = vacationRequestRepository.findById(requestId);
        if (vrOp.isPresent()) {
            if (currentUser.isAdmin() || currentUser.getId() - vrOp.get().getOriginalOwner().getId() == 0) {
                vacationRequestRepository.deleteById(requestId);
                return responseUtility.ok("Deleted", null);
            } else return responseUtility.forbidden();
        } else return responseUtility.notFound("Vacation Request Not Found");
    }

    private Boolean filterApprovedAndOwnerRequests(VacationRequest vr, HttpServletRequest request) {
        return  vr.onlyApproved() ||
                vr.getOriginalOwner().getId().equals(authService.currentUser(request).getId());
    }

}