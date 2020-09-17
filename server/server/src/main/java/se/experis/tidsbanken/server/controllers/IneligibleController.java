package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.repositories.IneligiblePeriodRepository;

import java.util.*;

import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.IneligiblePeriod;
import se.experis.tidsbanken.server.services.AuthorizationService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IneligibleController{

    @Autowired
    private IneligiblePeriodRepository ipRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/ineligible")
    public ResponseEntity<CommonResponse> getIneligiblePeriod(){
        try {
            return ResponseEntity.ok(
                    new CommonResponse("All ineligible periods", ipRepository.findAllByOrderByStartDesc()));
        } catch (Exception e) { return errorMessage(); }
    }

    @PostMapping("/ineligible")
    public ResponseEntity<CommonResponse> createIneligiblePeriod(@RequestBody IneligiblePeriod ip,
                                                                 HttpServletRequest request) {
        if(!authorizationService.isAuthorizedAdmin(request)) { return unauthorized(); }
        if (ipRepository.findAll().stream().allMatch(ip::excludesInPeriod)) {
            try {
                ipRepository.save(ip);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new CommonResponse("New period added", ip));
            } catch (Exception e) { return errorMessage(); }
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse("Period already exists"));
        }
    }

    @GetMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> getIneligiblePeriodForId(@PathVariable("ip_id") Long ip_id,
                                                                   HttpServletRequest request){
        if(!authorizationService.isAuthorizedAdmin(request)) { return unauthorized(); }
        Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
        if (fetchedPeriod.isPresent()){
            IneligiblePeriod period = fetchedPeriod.get();
            return ResponseEntity.ok(new CommonResponse("Ineligible period fetched successfully",
                    getResponseObject(period)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse("Ineligible period not found"));
        }
    }

    @PatchMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> updateIneligiblePeriod(@PathVariable("ip_id") Long ip_id,
                                                                 @RequestBody IneligiblePeriod ip,
                                                                 HttpServletRequest request) {
        if(!authorizationService.isAuthorizedAdmin(request)) { return unauthorized(); }
        Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
        if (fetchedPeriod.isPresent()) {
            IneligiblePeriod updatedPeriod = fetchedPeriod.get();
            updatedPeriod.start = ip.start;
            updatedPeriod.end = ip.end;
            updatedPeriod.moderator = ip.moderator;
            updatedPeriod.modified_at = new java.sql.Date(System.currentTimeMillis());
            try {
                ipRepository.save(updatedPeriod);
                return ResponseEntity.ok(new CommonResponse("Ineligible period updated successfully",
                        getResponseObject(updatedPeriod)));
            } catch (Exception e) { return errorMessage(); }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse("Ineligible period not found"));
        }
    }

    @DeleteMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> deleteIneligiblePeriod(@PathVariable("ip_id")long ip_id,
                                                                 HttpServletRequest request){
        Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
        if(!authorizationService.isAuthorizedAdmin(request)) { return unauthorized(); }
        if (fetchedPeriod.isPresent()) {
            try {
                ipRepository.deleteById(ip_id);
                return ResponseEntity.ok(new CommonResponse("Ineligible period deleted successfully"));
            } catch (Exception e) {
                e.printStackTrace();
                return errorMessage();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse("Ineligible period not found"));
        }
    }

    private ResponseEntity<CommonResponse> errorMessage() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse("Something went wrong when trying to process the request "));
    }

    private HashMap<String, Object> getResponseObject(IneligiblePeriod ip) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("period_start", ip.start);
        data.put("period_end", ip.end);
        data.put("moderator", ip.moderator);
        data.put("created_at", ip.created_at);
        data.put("modified_at", ip.modified_at);
        return data;
    }

    private ResponseEntity<CommonResponse> unauthorized() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponse( "Not Authorized"));
    }
}