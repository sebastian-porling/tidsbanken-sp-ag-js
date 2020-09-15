package se.experis.tidsbanken.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IneligibleController{
    @GetMapping("/ineligible")
    public ResponseEntity<IneligiblePeriod> getIneligiblePeriod(){
    }


    @PostMapping("/ineligible")
    public ResponseEntity<IneligiblePeriod> createIneligiblePeriod(@RequestBody IneligiblePeriod ineligibleperiod) {
    }

    @GetMapping("/ineligible/{ip_id}")
    public ResponseEntity<IneligiblePeriod> getIneligiblePeriodForId(@PathVariable("ip_id")long ip_id){
    }

    @PatchMapping("/ineligible/{ip_id}")
    public ResponseEntity<IneligiblePeriod> updateIneligiblePeriod(@RequestBody IneligiblePeriod ineligibleperiod) {
    }

    @DeleteMapping("/ineligible/{ip_id}")
    public ResponseEntity<IneligiblePeriod> deleteIneligiblePeriod(@PathVariable("ip_id")long ip_id){
    }

}