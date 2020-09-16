package se.experis.tidsbanken.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.VacationRequest;

@Controller
public class VacationController{
    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests(){
        return null;
    }

    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationrequest) {
        return null;
    }

    @GetMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id")long request_id){
        return null;
    }

    @PatchMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> updateRequest(@RequestBody VacationRequest vacationrequest) {
        return null;
    }

    @DeleteMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> deleteRequest(@PathVariable("request_id")long request_id){
        return null;
    }
}