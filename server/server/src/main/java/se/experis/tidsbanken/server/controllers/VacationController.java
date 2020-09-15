package se.experis.tidsbanken.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class VacationController{
    @GetMapping("/request")
    public ResponseEntity<VacationRequest> getRequests(){
    }

    @PostMapping("/request")
    public String createRequest(@RequestBody VacationRequest vacationrequest) {
    }

    @GetMapping("/request/{request_id}")
    public ResponseEntity<VacationRequest> getRequestsForId(@PathVariable("request_id")long request_id){
    }

    @PatchMapping("/request/{request_id}")
    public String updateRequest(@RequestBody VacationRequest vacationrequest) {
    }

    @DeleteMapping("/request/{request_id}")
    public ResponseEntity<VacationRequest> deleteRequest(@PathVariable("request_id")long request_id){
    }
}