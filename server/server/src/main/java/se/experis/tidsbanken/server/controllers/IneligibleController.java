package se.experis.tidsbanken.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import se.experis.tidsbanken.server.repositories.IneligiblePeriodRepository;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.socket.NotificationObserver;
import se.experis.tidsbanken.server.utils.ResponseUtility;


/**
 * Handles all functionality for Ineligible Periods
 */
@RestController
@RequestMapping("/api")
public class IneligibleController{
    @Autowired private IneligiblePeriodRepository ipRepository;
    @Autowired private AuthorizationService authService;
    @Autowired private ResponseUtility responseUtility;
    @Autowired private UserRepository userRepository;
    @Autowired private NotificationObserver observer;
    @Autowired private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    @Autowired private Validator validator = factory.getValidator();
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    /**
     * Returns all Ineligible Periods
     * @return 200 with all ineligible periods, 500 on server error
     */
    @GetMapping("/ineligible")
    public ResponseEntity<CommonResponse> getIneligiblePeriod(){
        try { return responseUtility.ok("All ineligible periods",
                    ipRepository.findAllByOrderByStartDesc());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch ineligible periods");
        }
    }

    /**
     * Creates a new ineligible period. Admin only action
     * @param ip Ineligible Periods Data
     * @param request HttpServletRequest
     * @return 201 and created ineligible periods, 400 if it overlaps with existing ineligible periods or not valid, 500 on server error
     */
    @PostMapping("/ineligible")
    public ResponseEntity<CommonResponse> createIneligiblePeriod(@RequestBody IneligiblePeriod ip,
                                                                 HttpServletRequest request) {
        if(!authService.isAuthorizedAdmin(request)) { return responseUtility.unauthorized(); }
        if (ipRepository.findAll().stream().allMatch(ip::excludesInPeriod)) {
            try {
                Set<ConstraintViolation<Object>> violations = validator.validate(ip);
                if(violations.isEmpty()) {
                    IneligiblePeriod saved = ipRepository.save(ip.setModerator(authService.currentUser(request)));
                    notify(saved, authService.currentUser(request), " has created an Ineligible Period");
                    return responseUtility.created("New period added", saved);
                }
                else return responseUtility.superBadRequest(violations);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return responseUtility.errorMessage("create ineligible period"); }
        } else return responseUtility.badRequest("Period already exists");
    }

    /**
     * Returns ineligible period by id
     * @param ip_id Ineligible Period Id
     * @return 200 with ineligible period, 404 if not found, 500 on server error
     */
    @GetMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> getIneligiblePeriodForId(@PathVariable("ip_id") Long ip_id){
        try {
            final Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
            if (fetchedPeriod.isPresent()){
                return responseUtility.ok("Ineligible period fetched successfully", fetchedPeriod.get());
            } else return responseUtility.notFound("Ineligible period not found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch ineligible period with id " + ip_id);
        }
    }

    /**
     * Updates the ineligible period with the provided id and data
     * @param ip_id Ineligible Period Id
     * @param ip Ineligible Period Data
     * @param request HttpServletRequest
     * @return 200 with updated data, 400 if overlapping or not valid, 401 if not admin, 404 if not found, 500 on server error
     */
    @PatchMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> updateIneligiblePeriod(@PathVariable("ip_id") Long ip_id,
                                                                 @RequestBody IneligiblePeriod ip,
                                                                 HttpServletRequest request) {
        try {
            if(!authService.isAuthorizedAdmin(request)) { return responseUtility.unauthorized(); }
            final Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
            if (fetchedPeriod.isPresent()) {
                if(fetchedPeriod.get().getStart().after(new Date())) {
                    Set<ConstraintViolation<Object>> violations = validator.validate(ip);
                    if(violations.isEmpty()) {
                        final IneligiblePeriod updatedPeriod = fetchedPeriod.get();
                        if (ip.getStart() != null && ip.getEnd() != null) {
                            if(ip.getStart().after(new Date())) updatedPeriod.setStart(ip.getStart());
                            else if(ip.getStart().before(new Date())) updatedPeriod.setEnd(ip.getEnd());
                            else return responseUtility.badRequest("Can't update period to a date that has passed.");
                        }
                        if(!ipRepository.findAllByIdNot(ip_id).stream().allMatch(updatedPeriod::excludesInPeriod))
                            return responseUtility.badRequest("Patched Ineligible Period Overlaps, Try Again");
                        updatedPeriod.setModerator(authService.currentUser(request));
                        updatedPeriod.setModifiedAt(new java.sql.Date(System.currentTimeMillis()));
                        try {
                            final IneligiblePeriod patchedIp = ipRepository.save(updatedPeriod);
                            notify(patchedIp, authService.currentUser(request), " has updated an Ineligible Period");
                            return responseUtility.ok("Ineligible period updated successfully", patchedIp);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            return responseUtility.errorMessage("update ineligible period with id " + ip_id);
                        }
                    }
                    else return responseUtility.superBadRequest(violations);
                } else {
                    return responseUtility.badRequest("Can't edit an ineligible period that has passed");
                }
            } else return responseUtility.notFound("Ineligible period not found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("update ineligible period with id " + ip_id);
        }
    }

    /**
     * Deletes the ineligible period by id
     * @param ip_id Ineligible Period Id
     * @param request HttpServletRequest
     * @return 200 if deleted, 401 if not admin, 404 if not found, 500 on server error
     */
    @DeleteMapping("/ineligible/{ip_id}")
    public ResponseEntity<CommonResponse> deleteIneligiblePeriod(@PathVariable("ip_id")long ip_id,
                                                                 HttpServletRequest request){
        try {
            final Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
            if(!authService.isAuthorizedAdmin(request)) { return responseUtility.unauthorized(); }
            if (fetchedPeriod.isPresent()) {
                try {
                    ipRepository.deleteById(ip_id);
                    notify(fetchedPeriod.get(), authService.currentUser(request), " has deleted a period");
                    return responseUtility.ok("Ineligible period deleted successfully", null);
                } catch (Exception e)  {
                    logger.error(e.getMessage());
                    return responseUtility.errorMessage("delete ineligible period with id " + ip_id);
                }
            } else return responseUtility.notFound("Ineligible period not found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("delete ineligible period with id " + ip_id);
        }
    }

    /**
     * Notifies all users except performer about the ineligible period
     * @param ip Ineligible Period to inform about
     * @param performer User who performed the action
     * @param message Message about the action
     */
    private void notify(IneligiblePeriod ip, User performer, String message) {
        userRepository.findAllByIsActiveTrue().stream()
                .filter(u -> !u.getId().equals(performer.getId()))
                .forEach(u -> observer.sendNotification(performer.getFullName() + message, u));
    }
}