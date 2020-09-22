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
import se.experis.tidsbanken.server.utils.ResponseUtility;


@Controller
public class IneligibleController{

    @Autowired private IneligiblePeriodRepository ipRepository;

    @Autowired private AuthorizationService authService;

    @Autowired private ResponseUtility responseUtility;

    @GetMapping("/ineligible")
    public ResponseEntity<CommonResponse> getIneligiblePeriod(HttpServletRequest request){
        if (!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        try { return responseUtility.ok("All ineligible periods",
                    ipRepository.findAllByOrderByStartDesc());
        } catch (Exception e) { return responseUtility.errorMessage(); }
    }

    @PostMapping("/ineligible")
    public ResponseEntity<CommonResponse> createIneligiblePeriod(@RequestBody IneligiblePeriod ip,
                                                                 HttpServletRequest request) {
        if(!authService.isAuthorizedAdmin(request)) { return responseUtility.unauthorized(); }
        if (ipRepository.findAll().stream().allMatch(ip::excludesInPeriod)) {
            try { ipRepository.save(ip.setModerator(authService.currentUser(request)));
                return responseUtility.created("New period added", ip);
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.badRequest("Period already exists");
    }

    @GetMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> getIneligiblePeriodForId(@PathVariable("ip_id") Long ip_id,
                                                                   HttpServletRequest request){
        if(!authService.isAuthorizedAdmin(request)) { return responseUtility.unauthorized(); }
        final Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
        if (fetchedPeriod.isPresent()){
            return responseUtility.ok("Ineligible period fetched successfully", fetchedPeriod.get());
        } else return responseUtility.notFound("Ineligible period not found");
    }

    @PatchMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> updateIneligiblePeriod(@PathVariable("ip_id") Long ip_id,
                                                                 @RequestBody IneligiblePeriod ip,
                                                                 HttpServletRequest request) {
        if(!authService.isAuthorizedAdmin(request)) { return responseUtility.unauthorized(); }
        final Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
        if (fetchedPeriod.isPresent()) {
            final IneligiblePeriod updatedPeriod = fetchedPeriod.get();
            if (ip.getStart() != null) updatedPeriod.setStart(ip.getStart());
            if (ip.getEnd() != null) updatedPeriod.setEnd(ip.getEnd());
            if(!ipRepository.findAllByIdNot(ip_id).stream().allMatch(updatedPeriod::excludesInPeriod))
                return responseUtility.badRequest("Patched Ineligible Period Overlaps, Try Again");
            updatedPeriod.setModerator(authService.currentUser(request));
            updatedPeriod.setModifiedAt(new java.sql.Date(System.currentTimeMillis()));
            try {
                IneligiblePeriod patchedIp = ipRepository.save(updatedPeriod);
                return responseUtility.ok("Ineligible period updated successfully", patchedIp);
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.notFound("Ineligible period not found");
    }

    @DeleteMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> deleteIneligiblePeriod(@PathVariable("ip_id")long ip_id,
                                                                 HttpServletRequest request){
        final Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
        if(!authService.isAuthorizedAdmin(request)) { return responseUtility.unauthorized(); }
        if (fetchedPeriod.isPresent()) {
            try {
                ipRepository.deleteById(ip_id);
                return responseUtility.ok("Ineligible period deleted successfully", null);
            } catch (Exception e)  {return responseUtility.errorMessage(); }
        } else return responseUtility.notFound("Ineligible period not found");
    }
}