package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import se.experis.tidsbanken.server.repositories.IneligiblePeriodRepository;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.socket.NotificationObserver;
import se.experis.tidsbanken.server.utils.ResponseUtility;


@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IneligibleController{

    @Autowired private IneligiblePeriodRepository ipRepository;

    @Autowired private AuthorizationService authService;

    @Autowired private ResponseUtility responseUtility;

    @Autowired private UserRepository userRepository;

    @Autowired private NotificationObserver observer;

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
            try {IneligiblePeriod saved = ipRepository.save(ip.setModerator(authService.currentUser(request)));
                notify(saved, authService.currentUser(request), " has created an Ineligible Period");
                return responseUtility.created("New period added", saved);
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
                final IneligiblePeriod patchedIp = ipRepository.save(updatedPeriod);
                notify(patchedIp, authService.currentUser(request), " has updated an Ineligible Period");
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
                notify(fetchedPeriod.get(), authService.currentUser(request), " has deleted a period");
                return responseUtility.ok("Ineligible period deleted successfully", null);
            } catch (Exception e)  {return responseUtility.errorMessage(); }
        } else return responseUtility.notFound("Ineligible period not found");
    }

    private void notify(IneligiblePeriod ip, User performer, String message) {
        userRepository.findAllByIsActiveTrue().stream()
                .filter(u -> !u.getId().equals(performer.getId()))
                .forEach(u -> observer.sendNotification(performer.getFullName() + message, u));
    }
}