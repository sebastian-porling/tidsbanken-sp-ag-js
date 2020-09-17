package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.VacationRequest;
=======
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.VacationRequestRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
>>>>>>> 8adbf446083741fbc0052cbc12c2124ebb037abd

@Controller
public class VacationController{

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests(){
<<<<<<< HEAD
        return null;
=======
        /*      X    User should be authenticated
                O    Curated list
                O    Chronological order (soonest first)
                O    Return first 50 by default
                X    All users can see: All approved requests
                X    All users can see: Their own request, regardless of state
                X    Admin can see: All request, regardless of state
         */
        CommonResponse cr = new CommonResponse();
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
                data.put("period_start", request.period_start);
                data.put("period_end", request.period_end);
                data.put("owner", request.owner.full_name);
                data.put("request_status", request.requestStatus);
                data.put("created_at", request.created_at);

                if(request.moderator != null) {
                    data.put("moderator", request.moderator.full_name);
                    data.put("moderation_date", request.moderation_date);
                }

                responseData.add(data);
            }

            cr.data = responseData;
            cr.message = "";
            cr.status = 200;
            resStatus = HttpStatus.OK;
        } else {
            cr.message = "No request found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(cr, resStatus);

>>>>>>> 8adbf446083741fbc0052cbc12c2124ebb037abd
    }

    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationrequest) {
<<<<<<< HEAD
        return null;
=======
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Validate body
               X     Search through existing request for that user to find one that overlaps
               X     Set status to pending
               X     return response
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
>>>>>>> 8adbf446083741fbc0052cbc12c2124ebb037abd
    }

    @GetMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id")long request_id){
<<<<<<< HEAD
        return null;
=======
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Search for existing request
               X     return curated request
               X     return response
                            403 Forbidden: if it is not the owner or admin OR if the state is not approved
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
>>>>>>> 8adbf446083741fbc0052cbc12c2124ebb037abd
    }

    @PatchMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> updateRequest(@RequestBody VacationRequest vacationrequest) {
<<<<<<< HEAD
        return null;
=======
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
>>>>>>> 8adbf446083741fbc0052cbc12c2124ebb037abd
    }

    @DeleteMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> deleteRequest(@PathVariable("request_id")long request_id){
<<<<<<< HEAD
        return null;
=======
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*      X     Search for existing request
               X     If admin
                            X     delete
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);

>>>>>>> 8adbf446083741fbc0052cbc12c2124ebb037abd
    }
}