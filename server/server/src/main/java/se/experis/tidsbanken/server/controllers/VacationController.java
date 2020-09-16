package se.experis.tidsbanken.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class VacationController{
    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests(){
    }

    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationrequest) {
    }

    @GetMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id")long request_id){
    }

    @PatchMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> updateRequest(@RequestBody VacationRequest vacationrequest) {
    }

    @DeleteMapping("/request/:request_id")
    public ResponseEntity<CommonResponse> deleteRequest(@PathVariable("request_id")long request_id){
    }
}