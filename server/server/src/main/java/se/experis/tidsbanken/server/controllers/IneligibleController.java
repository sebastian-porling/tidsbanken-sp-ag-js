package se.experis.tidsbanken.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.IneligiblePeriod;

@Controller
public class IneligibleController{
    @GetMapping("/ineligible")
    public ResponseEntity<CommonResponse> getIneligiblePeriod(){
        return null;
    }


    @PostMapping("/ineligible")
    public ResponseEntity<CommonResponse> createIneligiblePeriod(@RequestBody IneligiblePeriod ineligibleperiod) {
        return null;
    }

    @GetMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> getIneligiblePeriodForId(@PathVariable("ip_id")long ip_id){
        return null;
    }

    @PatchMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> updateIneligiblePeriod(@RequestBody IneligiblePeriod ineligibleperiod) {
        return null;
    }

    @DeleteMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> deleteIneligiblePeriod(@PathVariable("ip_id")long ip_id){
        return null;
    }

}