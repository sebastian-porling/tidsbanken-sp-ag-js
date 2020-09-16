package se.experis.tidsbanken.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IneligibleController{
    @GetMapping("/ineligible")
    public ResponseEntity<CommonResponse> getIneligiblePeriod(){
    }


    @PostMapping("/ineligible")
    public ResponseEntity<CommonResponse> createIneligiblePeriod(@RequestBody IneligiblePeriod ineligibleperiod) {
    }

    @GetMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> getIneligiblePeriodForId(@PathVariable("ip_id")long ip_id){
    }

    @PatchMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> updateIneligiblePeriod(@RequestBody IneligiblePeriod ineligibleperiod) {
    }

    @DeleteMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> deleteIneligiblePeriod(@PathVariable("ip_id")long ip_id){
    }

}