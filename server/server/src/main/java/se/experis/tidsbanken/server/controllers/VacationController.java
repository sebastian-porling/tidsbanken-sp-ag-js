package se.experis.tidsbanken.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.*;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.socket.NotificationObserver;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Handles all functionality for vacation requests
 */
@RestController
@RequestMapping("/api")
public class VacationController{

    @Autowired private VacationRequestRepository vrRepository;
    @Autowired private AuthorizationService authService;
    @Autowired private ResponseUtility responseUtility;
    @Autowired private CommentRepository commentRepository;
    @Autowired private IneligiblePeriodRepository ipRepository;
    @Autowired private SettingRepository settingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private NotificationObserver observer;
    @Autowired private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    @Autowired private Validator validator = factory.getValidator();
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    /**
     * Returns Vacation Requests based on parameters for pageable
     * @param request HttpServletRequest
     * @param date Date to start from
     * @param page which page to get
     * @param amount the amount on the page
     * @return 200 with vaction requests, only approved for normal users, 500 on server error
     */
    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests( HttpServletRequest request,
                                                      @RequestParam("date") Optional<String> date,
                                                      @RequestParam("page") Optional<Integer> page,
                                                      @RequestParam("amount") Optional<Integer> amount){
        try {
            final List<VacationRequest> allVacationRequests = vrRepository
                    .findAllByOrderByStartDesc(PageRequest.of(page.orElse(0), amount.orElse(50)));
            if (authService.currentUser(request).isAdmin())
                return responseUtility.ok("All vacation requests", allVacationRequests);
            return responseUtility.ok(
                    "All vacation requests",
                    allVacationRequests.stream().filter((vr) ->
                        filterApprovedAndOwnerRequests(vr, request))
                        .collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch requests");
        }
    }

    /**
     * Creates a vacation request with provided data
     * @param vacationRequest Vacation Request
     * @param request HttpServletRequest
     * @return 200 if created with created vacation request, 400 on validation error or overlapping periods, 500 on server error
     */
    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        try {
            if (vacationRequest.getTitle() != null && vacationRequest.getStart() != null && vacationRequest.getEnd() != null ) {
                final User currentUser = authService.currentUser(request);
                final List<VacationRequest> vacationRequests = vrRepository.findAllByOwner(currentUser);
                final List<IneligiblePeriod> ips = ipRepository.findAllByOrderByStartDesc();
                if( vacationRequest.getStart().before(vacationRequest.getEnd())) {
                    if (vacationRequests.stream().allMatch(vacationRequest::excludesInPeriod) &&
                            ips.stream().allMatch(vacationRequest::excludesInIP)) {
                        try {
                            Set<ConstraintViolation<Object>> violations = validator.validate(vacationRequest);
                            if(violations.isEmpty()) {
                                final VacationRequest vr = vrRepository
                                        .save(vacationRequest.setStatus(StatusType.PENDING.getStatus()).setOwner(currentUser));
                                return responseUtility.created("Created", vr);
                            }
                            else return responseUtility.superBadRequest(violations);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            return responseUtility.errorMessage("create request");
                        }
                    } else { return responseUtility.badRequest("Overlaps with existing requests"); }
                } else { return responseUtility.badRequest("End date can't be before start date."); }
            } else { return responseUtility.badRequest("Could not process request. Missing some fields."); }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("create request");
        }
    }

    /**
     * Returns vacation request by id
     * @param requestId Vacation Request Id
     * @param request HttpServletRequest
     * @return 200 with vacation request, 403 if not allowed to get resource, 404 if not found, 500 on server error
     */
    @GetMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id") Long requestId,
                                                           HttpServletRequest request){
        try {
            final Optional<VacationRequest> vacationRequest = vrRepository.findById(requestId);
            if (vacationRequest.isPresent()) {
                final VacationRequest vr = vacationRequest.get();
                if (authService.isAuthorizedAdmin(request))
                    return responseUtility.ok("Vacation Request Found", vr);
                if (filterApprovedAndOwnerRequests(vr, request)) {
                    return responseUtility.ok("Vacation Request Found", vr);
                } else return responseUtility.forbidden("Not authorized to view this request.");
            }
            return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch request with id " + requestId);
        }
    }

    /**
     * Updated a vacation request with any number of parameters.
     * Status can only be updated by non current user administrators
     * @param requestId Vacation Request Id
     * @param vacationRequest Vacation Request Data to update
     * @param request HttpServletRequest
     * @return 200 if updated, 400 if validation error or overlapping periods, 404 if not found, 403 if forbidden action, 500 on server error
     */
    @PatchMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> updateRequest(@PathVariable("request_id") Long requestId,
                                                        @RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        try {
            if (!authService.isAuthorized(request)) return responseUtility.unauthorized();
            final Optional<VacationRequest> vrOp = vrRepository.findById(requestId);
            if (vrOp.isPresent()) {
                final VacationRequest vr = vrOp.get();
                if(!vr.isPending()) return responseUtility.forbidden("Not authorized to update a request that is " +
                        "moderated.");
                final boolean isAdmin = authService.isAuthorizedAdmin(request);
                final User currentUser = authService.currentUser(request);
                final boolean isOwner = vr.getOriginalOwner().getId().equals(currentUser.getId());
                if (isAdmin || isOwner) {
                    try {
                        if (vacationRequest.getTitle() != null) vr.setTitle(vacationRequest.getTitle());
                        if (vacationRequest.getStart() != null) vr.setStart(vacationRequest.getStart());
                        if (vacationRequest.getEnd() != null) vr.setEnd(vacationRequest.getEnd());
                        if (vacationRequest.getStart() != null || vacationRequest.getEnd() != null) {
                            if (!vrRepository.findAllByOwnerAndIdNot(vr.getOriginalOwner(), vr.getId()).stream().allMatch(vr::excludesInPeriod)
                                    || !ipRepository.findAllByOrderByStartDesc().stream().allMatch(vr::excludesInIP)) {
                                return responseUtility.badRequest("Patched Vacation Request Overlaps");
                            } else if( vacationRequest.getStart().after(vacationRequest.getEnd())) {
                                return responseUtility.badRequest("End date can't be before start date.");
                            }
                        }
                        if (isAdmin && !isOwner && vacationRequest.getStatus() != null) {
                            vr.setStatus(vacationRequest.getStatus());
                            vr.setModerationDate(new Date(System.currentTimeMillis()));
                            vr.setModerator(currentUser);
                            if (vacationRequest.getStatus() != null && vacationRequest.onlyApproved()) {
                                final Long days = TimeUnit.DAYS.convert(vr.getEnd().getTime() - vr.getStart().getTime(), TimeUnit.MILLISECONDS);
                                final int userDays = vr.getOriginalOwner().getVacationDays() - vr.getOriginalOwner().getUsedVacationDays();
                                if (days > userDays) return responseUtility.badRequest("Not enough vacation days");
                                final Optional<Setting> maxVacationDays = settingRepository.findByKey("maxVacationDays");
                                maxVacationDays.ifPresent(v -> System.out.println((Long.parseLong((String) v.getValue()) < days) + " " + Long.parseLong((String) v.getValue()) + " " + days) );
                                if (maxVacationDays.isPresent() && Long.parseLong((String) maxVacationDays.get().getValue()) < days)
                                    return responseUtility.badRequest("Request exceeds vacation day limit!");
                                vr.getOriginalOwner().setUsedVacationDays(vr.getOriginalOwner().getUsedVacationDays()+days.intValue());
                                userRepository.save(vr.getOriginalOwner());
                            }
                        } else {if (vacationRequest.getStatus() != null && !vacationRequest.isPending()) return responseUtility.forbidden("Not authorized to change status on this request."); }
                        vr.setModifiedAt(new Date(System.currentTimeMillis()));
                        Set<ConstraintViolation<Object>> violations = validator.validate(vacationRequest);
                        if(violations.isEmpty()) {
                            final VacationRequest patchedVr = vrRepository.save(vr);
                            notify(patchedVr, currentUser, " modified Vacation Request ");
                            return responseUtility.ok("Updated", patchedVr);
                        }
                        else return responseUtility.superBadRequest(violations);
                    } catch(Exception e) {
                        logger.error(e.getMessage());
                        return responseUtility.errorMessage("update request with id " + requestId); }
                } else return responseUtility.forbidden("Not authorized to edit this request.");
            } else return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("update request with id " + requestId);
        }
    }

    /**
     * Deletes a vacation request and the comments associated with it.
     * Works only for admin users
     * @param requestId Vacation Request id
     * @param request HttpServletRequest
     * @return 200 if it exists, 404 if it doesn't exist, 500 if server error
     */
    @DeleteMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> deleteRequest(@PathVariable("request_id") Long requestId,
                                                        HttpServletRequest request){
        try {
            if (!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
            final User currentUser = authService.currentUser(request);
            final Optional<VacationRequest> vrOp = vrRepository.findById(requestId);
            if (vrOp.isPresent()) {
                commentRepository.findAllByRequestOrderByCreatedAtAsc(vrOp.get()).forEach(commentRepository::delete);
                vrRepository.delete(vrOp.get());
                notify(vrOp.get(), currentUser, " deleted Vacation Request ");
                return responseUtility.ok("Deleted", null);
            } else return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("delete request with id " + requestId);
        }
    }

    /**
     * Filters the vacation request by only approved or all owned by the current user
     * @param vr Vacation Request
     * @param request HttpServletRequest
     * @return true if approved or owned by current user of http request
     */
    private Boolean filterApprovedAndOwnerRequests(VacationRequest vr, HttpServletRequest request) {
        return  vr.onlyApproved() ||
                vr.getOriginalOwner().getId().equals(authService.currentUser(request).getId());
    }

    /**
     * Sends a notification to all users associated with the vacation request except the performer
     * @param vr Vacation Request
     * @param performer User
     * @param message String
     */
    private void notify(VacationRequest vr, User performer, String message) {
        final List<Comment> comments = commentRepository.findAllByRequestOrderByCreatedAtAsc(vr);
        final List<User> users = comments.stream().map(Comment::getOriginalUser).collect(Collectors.toList());
        users.add(vr.getOriginalOwner());
        Optional.ofNullable(vr.getOriginalModerator()).ifPresent(users::add);
        users.stream().filter(u -> !u.getId().equals(performer.getId()))
                .forEach(u -> observer.sendNotification(performer.getFullName() + message + vr.getTitle(), u));
    }

}