package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.VacationRequestRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Controller
public class VacationController{

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests(){
        /*      X    User should be authenticated
                O    Curated list
                O    Chronological order (soonest first)
                O    Return first 50 by default
                X    All users can see: All approved requests
                X    All users can see: Their own request, regardless of state
                X    Admin can see: All request, regardless of state
         */
        CommonResponse cr;
        HttpStatus resStatus;

        List<VacationRequest> allVacationRequests = vacationRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "period_start"));
        List<VacationRequest> firstN = new ArrayList<>();
        List<HashMap> responseData = new ArrayList<>();

        int n = 50;

        if(allVacationRequests.size() > 0) {
            if (allVacationRequests.size() < n) n = allVacationRequests.size();

            for (int i = 0; i < n; i++) {
                firstN.add(allVacationRequests.get(i));
            }

            for (VacationRequest request : firstN) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("title", request.title);
                data.put("period_start", request.start);
                data.put("period_end", request.end);
                data.put("owner", request.owner.full_name);
                data.put("request_status", request.requestStatus);
                data.put("created_at", request.created_at);

                if(request.moderator != null) {
                    data.put("moderator", request.moderator.full_name);
                    data.put("moderation_date", request.moderation_date);
                }

                responseData.add(data);
            }
            cr = new CommonResponse("Success", responseData);
            resStatus = HttpStatus.OK;
        } else {
            cr = new CommonResponse("No request found");
            resStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(cr, resStatus);
    }

    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationrequest) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Validate body
               X     Search through existing request for that user to find one that overlaps
               X     Set status to pending
               X     return response
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @GetMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id")long request_id){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Search for existing request
               X     return curated request
               X     return response
                            403 Forbidden: if it is not the owner or admin OR if the state is not approved
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @PatchMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> updateRequest(@RequestBody VacationRequest vacationrequest) {
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

    @DeleteMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> deleteRequest(@PathVariable("request_id")long request_id){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Search for existing request
               X     If admin
                            X     delete
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);

    }
}