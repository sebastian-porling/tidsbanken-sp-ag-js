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

@Controller
public class VacationController{

    @Autowired private VacationRequestRepository vrRepository;
    @Autowired private AuthorizationService authService;
    @Autowired private ResponseUtility responseUtility;
    @Autowired private CommentRepository commentRepository;
    @Autowired private IneligiblePeriodRepository ipRepository;
    @Autowired private SettingRepository settingRepository;
    @Autowired private NotificationObserver observer;
    @Autowired private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    @Autowired private Validator validator = factory.getValidator();
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    @GetMapping("/request")
    public ResponseEntity<CommonResponse> getRequests( HttpServletRequest request,
                                                      @RequestParam("date") Optional<String> date,
                                                      @RequestParam("page") Optional<Integer> page,
                                                      @RequestParam("amount") Optional<Integer> amount){
        /*
        * TODO - Date searches
        * [ ] - Add date search
        * [ ] - Add between two dates search
        */
        try {
            if (!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
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

    @PostMapping("/request")
    public ResponseEntity<CommonResponse> createRequest(@RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        try {
            if (!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
            final User currentUser = authService.currentUser(request);
            final List<VacationRequest> vacationRequests = vrRepository.findAllByOwner(currentUser);
            final List<IneligiblePeriod> ips = ipRepository.findAllByOrderByStartDesc();
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
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("create request");
        }
    }

    @GetMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> getRequestsForId(@PathVariable("request_id") Long requestId,
                                                           HttpServletRequest request){
        try {
            if (!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
            final Optional<VacationRequest> vacationRequest = vrRepository.findById(requestId);
            if (vacationRequest.isPresent()) {
                final VacationRequest vr = vacationRequest.get();
                if (authService.isAuthorizedAdmin(request))
                    return responseUtility.ok("Vacation Request Found", vr);
                if (filterApprovedAndOwnerRequests(vr, request)) {
                    return responseUtility.ok("Vacation Request Found", vr);
                } else return responseUtility.forbidden();
            }
            return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch request with id " + requestId);
        }
    }

    @PatchMapping("/request/{request_id}")
    public ResponseEntity<CommonResponse> updateRequest(@PathVariable("request_id") Long requestId,
                                                        @RequestBody VacationRequest vacationRequest,
                                                        HttpServletRequest request) {
        try {
            if (!authService.isAuthorized(request)) return responseUtility.unauthorized();
            final Optional<VacationRequest> vrOp = vrRepository.findById(requestId);
            if (vrOp.isPresent()) {
                final VacationRequest vr = vrOp.get();
                if(!vr.isPending()) return responseUtility.forbidden();
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
                                    || !ipRepository.findAllByOrderByStartDesc().stream().allMatch(vr::excludesInIP))
                                return responseUtility.badRequest("Patched Vacation Request Overlaps");
                        }
                        if (isAdmin && !isOwner) {
                            if (vacationRequest.getStatus() != null && !vacationRequest.isPending()) {
                                vr.setStatus(vacationRequest.getStatus());
                                vr.setModerationDate(new Date(System.currentTimeMillis()));
                                vr.setModerator(currentUser);
                                final Long days = TimeUnit.DAYS.convert(vr.getEnd().getTime() - vr.getStart().getTime(), TimeUnit.MILLISECONDS);
                                final int userDays = vr.getOriginalOwner().getVacationDays() - vr.getOriginalOwner().getUsedVacationDays();
                                if (days > userDays) return responseUtility.badRequest("Not enough vacation days");
                                final Optional<Setting> maxVacationDays = settingRepository.findByKey("maxVacationDays");
                                maxVacationDays.ifPresent(v -> System.out.println((Long.parseLong((String) v.getValue()) < days) + " " + Long.parseLong((String) v.getValue()) + " " + days) );
                                if (maxVacationDays.isPresent() && Long.parseLong((String) maxVacationDays.get().getValue()) < days)
                                    return responseUtility.badRequest("Request exceeds vacation day limit!");
                                vr.getOriginalOwner().setUsedVacationDays(vr.getOriginalOwner().getUsedVacationDays()+days.intValue());
                            }
                        } else {if (vacationRequest.getStatus() != null && !vacationRequest.isPending()) return responseUtility.forbidden(); }
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
                } else return responseUtility.forbidden();
            } else return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("update request with id " + requestId);
        }
    }

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

    private Boolean filterApprovedAndOwnerRequests(VacationRequest vr, HttpServletRequest request) {
        return  vr.onlyApproved() ||
                vr.getOriginalOwner().getId().equals(authService.currentUser(request).getId());
    }

    private void notify(VacationRequest vr, User performer, String message) {
        final List<Comment> comments = commentRepository.findAllByRequestOrderByCreatedAtAsc(vr);
        final List<User> users = comments.stream().map(Comment::getOriginalUser).collect(Collectors.toList());
        users.add(vr.getOriginalOwner());
        Optional.ofNullable(vr.getOriginalModerator()).ifPresent(users::add);
        users.stream().filter(u -> !u.getId().equals(performer.getId()))
                .forEach(u -> observer.sendNotification(performer.getFullName() + message + vr.getTitle(), u));
    }

}