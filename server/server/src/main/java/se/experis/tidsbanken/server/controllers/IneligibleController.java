package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import se.experis.tidsbanken.server.repositories.IneligiblePeriodRepository;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.services.AuthorizationService;



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
                    new CommonResponse("All ineligible periods",
                            ipRepository.findAllByOrderByStartDesc()));
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
            return ResponseEntity.ok(new CommonResponse(
                    "Ineligible period fetched successfully",
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
            if (ip.getStart() != null) updatedPeriod.setStart(ip.getStart());
            if (ip.getEnd() != null) updatedPeriod.setEnd(ip.getEnd());
            if (ip.getModerator() != null) updatedPeriod.setModerator(ip.getOriginalModerator());
            updatedPeriod.setModifiedAt(new java.sql.Date(System.currentTimeMillis()));
            try {
                ipRepository.save(updatedPeriod);
                return ResponseEntity.ok(new CommonResponse(
                        "Ineligible period updated successfully",
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
                return ResponseEntity.ok(new CommonResponse(
                        "Ineligible period deleted successfully"));
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
                .body(new CommonResponse(
                        "Something went wrong when trying to process the request "));
    }

    private HashMap<String, Object> getResponseObject(IneligiblePeriod ip) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("period_start", ip.getStart());
        data.put("period_end", ip.getEnd());
        data.put("moderator", ip.getModerator());
        data.put("created_at", ip.getCreatedAt());
        data.put("modified_at", ip.getModifiedAt());
        return data;
    }

    private ResponseEntity<CommonResponse> unauthorized() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponse( "Not Authorized"));
    }
}