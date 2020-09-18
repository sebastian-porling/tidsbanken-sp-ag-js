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
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class VacationController{

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests(HttpServletRequest request,
                                                      @RequestParam("date") Optional<String> date,
                                                      @RequestParam("page") Optional<Integer> page,
                                                      @RequestParam("amount") Optional<Integer> amount){
        /*
        * TODO
        * [ ] - Add date search
        * [ } - Add between two dates search
        */
        List<VacationRequest> allVacationRequests = vacationRequestRepository
                .findAllByOrderByStartDesc(PageRequest.of(page.orElse(0), amount.orElse(50)));
        if (!authorizationService.isAuthorized(request)) { return unauthorized(); }
        if (authorizationService.currentUser(request).isAdmin()) {
            return ResponseEntity.ok(new CommonResponse(
                    "All vacation requests", allVacationRequests));
        } else {
            return ResponseEntity.ok(new CommonResponse(
                    "All vacation requests",
                    allVacationRequests.stream().filter((vr) ->
                    vr.getOriginalOwner().getId()
                            .equals(authorizationService.currentUser(request).getId()))
                    .collect(Collectors.toList())));
        }
    }

    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationrequest,
                                                        HttpServletRequest request) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Validate body
               X     Search through existing request for that user to find one that overlaps
               X     Set status to pending
               X     return response
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @GetMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id") Long request_id,
                                                           HttpServletRequest request){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Search for existing request
               X     return curated request
               X     return response
                            403 Forbidden: if it is not the owner or admin OR if the state is not approved
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @PatchMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> updateRequest(@PathVariable("request_id") Long requestId,
                                                        @RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Search for existing request
               X     validate body
               X     if not moderated
                            X     update modified_at
                            X     save changes
                            X     return response
               X     Also if admin
                            X     update status, moderation_date, moderator
                            X     return response 200 OK || 403 if not admin
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @DeleteMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> deleteRequest(@PathVariable("request_id") Long requestId,
                                                        HttpServletRequest request){
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
}