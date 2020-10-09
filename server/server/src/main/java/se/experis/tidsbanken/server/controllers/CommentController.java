package se.experis.tidsbanken.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles all functionality for comments
 */
@RestController
@RequestMapping("/api")
public class CommentController{

    @Autowired private CommentRepository commentRepository;
    @Autowired private VacationRequestRepository requestRepository;
    @Autowired private AuthorizationService authService;
    @Autowired private ResponseUtility responseUtility;
    @Autowired private NotificationObserver observer;
    @Autowired private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    @Autowired private Validator validator = factory.getValidator();
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    /**
     * Returns all comments associated with the given vacation request id
     * @param requestId Vacation Request id
     * @param request HttpServletRequest
     * @return 200 with all comments, 403 if not admin or owner, 500 on server error
     */
    @GetMapping("/request/{request_id}/comment")
    public ResponseEntity<CommonResponse> getComments(@PathVariable("request_id") Long requestId,
                                                      HttpServletRequest request){
        try{
            final Optional<VacationRequest> vacationRequestOp = requestRepository.findById(requestId);
            if (vacationRequestOp.isPresent()) {
                final VacationRequest vr = vacationRequestOp.get();
                if (isRequestOwner(vr, request) || authService.isAuthorizedAdmin(request)) {
                    final List<Comment> comments = commentRepository.findAllByRequestOrderByCreatedAtAsc(vr);
                    return responseUtility.ok("All Comments For Request: " + vr.getId(), comments);
                } else return responseUtility.forbidden("Not authorized to fetch comments for this post");
            } else return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch comments");
        }
    }

    /**
     * Creates a new comment on the given vacation request id
     * @param requestId Vacation Request Id
     * @param comment Comment data
     * @param request HttpServletReqeust
     * @return 201 with created comment, 400 on not valid data, 403 if not vacation request or admin, 404 if vacation request doesn't exist, 500 on server error
     */
    @PostMapping("/request/{request_id}/comment")
    public ResponseEntity<CommonResponse> createComment(@PathVariable("request_id") Long requestId,
                                                        @RequestBody Comment comment,
                                                        HttpServletRequest request) {
        try{
            final Optional<VacationRequest> vacationRequestOp = requestRepository.findById(requestId);
            if (vacationRequestOp.isPresent()) {
                final VacationRequest vr = vacationRequestOp.get();
                final User currentUser = authService.currentUser(request);
                if (isRequestOwner(vr, request) || authService.isAuthorizedAdmin(request)) {
                    try{
                        Set<ConstraintViolation<Object>> violations = validator.validate(comment);
                        if(violations.isEmpty()) {
                            final Comment saved = commentRepository.save(comment.setUser(currentUser).setRequest(vr));
                            notifyUsers(vr, currentUser, " has commented on ");
                            return responseUtility.ok("Saved Comment", saved);
                        }
                        else return responseUtility.superBadRequest(violations);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        return responseUtility.errorMessage("create comment"); }
                } else return responseUtility.forbidden("Not authorized to create comment on this post.");
            } else return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("create comment");
        }
    }

    /**
     * Returns the comment by id on vacation request
     * @param requestId Vacation Request Id
     * @param commentId Comment Id
     * @param request HttpServletRequest
     * @return 200 with comment data, 403 if not admin or owner, 404 if vacation request or comment not found, 500 on server error
     */
    @GetMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> getComment(@PathVariable("request_id") Long requestId,
                                                     @PathVariable("comment_id") Long commentId,
                                                     HttpServletRequest request){
        final Optional<VacationRequest> vrOp = requestRepository.findById(requestId);
        if (vrOp.isPresent()) {
            if (!authService.isAuthorizedAdmin(request) && !isRequestOwner(vrOp.get(), request))
                return responseUtility.forbidden("Not authorized to fetch comment.");
            try {
                final Optional<Comment> commentOp = commentRepository.findByIdAndRequestOrderByCreatedAtAsc(commentId, vrOp.get());
                return commentOp.map(comment -> responseUtility
                        .ok("Successfully retrieved comment", comment))
                        .orElseGet(() -> responseUtility.notFound("Comment Not Found"));
            } catch (Exception e) {
                logger.error(e.getMessage());
                return responseUtility.errorMessage("fetch comment with id " + commentId);
            }
        } else return responseUtility.notFound("Vacation Request Not Found");
    }

    /**
     * Updates comment if not passed 24 hours and is owner
     * @param requestId VacationRequest Id
     * @param commentId Comment Id
     * @param comment Comment Data
     * @param request HttpServletRequest
     * @return 200 with updated comment, 400 on not valid data, 403 if not owner, 404 if vacation request or comment not found, 500 on server error
     */
    @PatchMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable("request_id") Long requestId,
                                                        @PathVariable("comment_id") Long commentId,
                                                        @RequestBody Comment comment,
                                                        HttpServletRequest request) {
        final Optional<VacationRequest> vrOp = requestRepository.findById(requestId);
        if(vrOp.isPresent()) {
            try {
                final Optional<Comment> commentOp = commentRepository.findByIdAndRequestOrderByCreatedAtAsc(commentId, vrOp.get());
                if (commentOp.isPresent()) {
                    final Comment patchComment = commentOp.get();
                    if(isCommentOwner(patchComment, request)) {
                        if(isPastTwentyFourHours(patchComment)) {
                            if(comment.getMessage() != null) patchComment.setMessage(comment.getMessage());
                            patchComment.updateModifiedAt();
                            final Comment updatedComment = commentRepository.save(patchComment);
                            notifyUsers(vrOp.get(), authService.currentUser(request), " has updated their comment on ");
                            return responseUtility.ok("Updated", updatedComment);
                        } else return responseUtility.forbidden("Not allowed to edit comments after 24 hours.");
                    } else return responseUtility.forbidden("Can not edit comments from other users.");
                } else return responseUtility.notFound("Comment Not Found");
            } catch (Exception e) {
                logger.error(e.getMessage());
                return responseUtility.errorMessage("update comment with id " + commentId);
            }
        } else return responseUtility.notFound("Vacation Request Not Found");
    }

    /**
     * Deletes the comment by vacation request id and comment id
     * @param requestId Vacation Request id
     * @param commentId Comment id
     * @param request HttpServletRequest
     * @return 200 if deleted, 403 if not admin or owner, 404 if vacation request or comment not found, 500 on server error
     */
    @DeleteMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable("request_id") Long requestId,
                                                        @PathVariable("comment_id") Long commentId,
                                                        HttpServletRequest request){
        try {
            final Optional<VacationRequest> vrOp = requestRepository.findById(requestId);
            if (vrOp.isPresent()) {
                final Optional<Comment> commentOp = commentRepository.findByIdAndRequestOrderByCreatedAtAsc(commentId, vrOp.get());
                if (commentOp.isPresent()) {
                    if (!authService.isAuthorizedAdmin(request) && !isCommentOwner(commentOp.get(), request))
                        return responseUtility.forbidden("Not authorized to delete comment.");
                    if (isPastTwentyFourHours(commentOp.get())) {
                        commentRepository.delete(commentOp.get());
                        notifyUsers(vrOp.get(), authService.currentUser(request), " has deleted their comment on ");
                        return responseUtility.ok("Deleted", null);
                    } else return responseUtility.badRequest("Can't delete older than 24 hours");
                } else return responseUtility.notFound("Comment Not Found");
            } else return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("delete comment with id " + commentId);
        }
    }

    /**
     * Checks if the current user is vacation request owner
     * @param vacationRequest Vacation Request
     * @param request HttpServletReqeuest
     * @return true if owner
     */
    private boolean isRequestOwner(VacationRequest vacationRequest, HttpServletRequest request) {
        return authService.currentUser(request).getId() - vacationRequest.getOriginalOwner().getId() == 0;
    }

    /**
     * Checks if the current is is comment owner
     * @param comment Comment
     * @param request HttpServletRequest
     * @return true if owner
     */
    private boolean isCommentOwner(Comment comment, HttpServletRequest request) {
        return authService.currentUser(request).getId() - comment.getOriginalUser().getId() == 0;
    }

    /**
     * Checks if the creation date is past 24 hours
     * @param comment Comment
     * @return true if past twenty four hours
     */
    private boolean isPastTwentyFourHours(Comment comment) {
        final long DAY = 24 * 60 * 60 * 1000;
        return comment.getCreatedAt().after(new Timestamp(System.currentTimeMillis() - DAY));
    }

    /**
     * Notifies all users associated with the vacation request except the performer
     * @param vr Vacation Request
     * @param performer User who made a comment
     * @param message Message to inform the users
     */
    private void notifyUsers(VacationRequest vr, User performer, String message) {
        final List<Comment> comments = commentRepository.findAllByRequestOrderByCreatedAtAsc(vr);
        final List<User> users = comments.stream().map(Comment::getOriginalUser).collect(Collectors.toList());
        users.add(vr.getOriginalOwner());
        if(vr.getOriginalModerator() != null) users.add(vr.getOriginalModerator());
        users.stream().distinct().filter(u -> !u.getId().equals(performer.getId())).forEach(u -> {
            observer.sendNotification(performer.getFullName() + message + vr.getTitle(), u);
        });
    }
}