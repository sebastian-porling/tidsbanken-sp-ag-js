package se.experis.tidsbanken.server.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.CommentRepository;
import se.experis.tidsbanken.server.repositories.VacationRequestRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
public class CommentController{

    @Autowired private CommentRepository commentRepository;

    @Autowired private VacationRequestRepository requestRepository;

    @Autowired private AuthorizationService authService;

    @Autowired private ResponseUtility responseUtility;

    @GetMapping("/request/{request_id}/comment")
    public ResponseEntity<CommonResponse> getComments(@PathVariable("request_id") Long requestId,
                                                      HttpServletRequest request){
        if(!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        try{
            final Optional<VacationRequest> vacationRequestOp = requestRepository.findById(requestId);
            if (vacationRequestOp.isPresent()) {
                final VacationRequest vr = vacationRequestOp.get();
                if (isRequestOwner(vr, request) || authService.isAuthorizedAdmin(request)) {
                    final List<Comment> comments = commentRepository.findAllByRequest(vr);
                    return responseUtility.ok("All Comments For Request: " + vr.getId(), comments);
                } else return responseUtility.forbidden();
            } else return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) { return responseUtility.errorMessage(); }
    }

    @PostMapping("/request/{request_id}/comment")
    public ResponseEntity<CommonResponse> createComment(@PathVariable("request_id") Long requestId,
                                                        @RequestBody Comment comment,
                                                        HttpServletRequest request) {
        if(!authService.isAuthorized(request)) { responseUtility.unauthorized(); }
        try{
            final Optional<VacationRequest> vacationRequestOp = requestRepository.findById(requestId);
            if (vacationRequestOp.isPresent()) {
                final VacationRequest vr = vacationRequestOp.get();
                final User currentUser = authService.currentUser(request);
                if (isRequestOwner(vr, request) || authService.isAuthorizedAdmin(request)) {
                    try{
                        return responseUtility.ok("Saved Comment",
                                commentRepository.save(comment.setUser(currentUser).setRequest(vr)));
                    } catch (Exception e) { return responseUtility.errorMessage(); }
                } else return responseUtility.forbidden();
            } else return responseUtility.notFound("Vacation Request Not Found");
        } catch (Exception e) { return responseUtility.errorMessage(); }
    }

    @GetMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> getComment(@PathVariable("request_id") Long requestId,
                                                     @PathVariable("comment_id") Long commentId,
                                                     HttpServletRequest request){
        if(!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final Optional<VacationRequest> vrOp = requestRepository.findById(requestId);
        if (vrOp.isPresent()) {
            if (!authService.isAuthorizedAdmin(request) && !isRequestOwner(vrOp.get(), request))
                return responseUtility.forbidden();
            try {
                final Optional<Comment> commentOp = commentRepository.findByIdAndRequest(commentId, vrOp.get());
                return commentOp.map(comment -> responseUtility
                        .ok("Successfully retrieved comment", comment))
                        .orElseGet(() -> responseUtility.notFound("Comment Not Found"));
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.notFound("Vacation Request Not Found");
    }

    @PatchMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable("request_id") Long requestId,
                                                        @PathVariable("comment_id") Long commentId,
                                                        @RequestBody Comment comment,
                                                        HttpServletRequest request) {
        if(!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final Optional<VacationRequest> vrOp = requestRepository.findById(requestId);
        if(vrOp.isPresent()) {
            try {
                final Optional<Comment> commentOp = commentRepository.findByIdAndRequest(commentId, vrOp.get());
                if (commentOp.isPresent()) {
                    final Comment patchComment = commentOp.get();
                    if(isCommentOwner(patchComment, request) && isPastTwentyFourHours(patchComment)) {
                        if(comment.getMessage() != null) patchComment.setMessage(comment.getMessage());
                        patchComment.updateModifiedAt();
                        commentRepository.save(patchComment);
                        return responseUtility.ok("Updated", null);
                    } else return responseUtility.forbidden();
                } else return responseUtility.notFound("Comment Not Found");
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.notFound("Vacation Request Not Found");
    }

    @DeleteMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable("request_id") Long requestId,
                                                        @PathVariable("comment_id") Long commentId,
                                                        HttpServletRequest request){
        if(!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final Optional<VacationRequest> vrOp = requestRepository.findById(requestId);
        if (vrOp.isPresent()) {
            final Optional<Comment> commentOp = commentRepository.findByIdAndRequest(commentId, vrOp.get());
            if (commentOp.isPresent()) {
                if (!authService.isAuthorizedAdmin(request) && !isCommentOwner(commentOp.get(), request))
                    return responseUtility.forbidden();
                if (isPastTwentyFourHours(commentOp.get())) {
                    commentRepository.delete(commentOp.get());
                    return responseUtility.ok("Deleted", null);
                } else return responseUtility.badRequest("Can't delete older than 24 hours");
            } else return responseUtility.notFound("Comment Not Found");
        } else return responseUtility.notFound("Vacation Request Not Found");
    }

    private boolean isRequestOwner(VacationRequest vacationRequest, HttpServletRequest request) {
        return authService.currentUser(request).getId() - vacationRequest.getOriginalOwner().getId() == 0;
    }

    private boolean isCommentOwner(Comment comment, HttpServletRequest request) {
        return authService.currentUser(request).getId() - comment.getOriginalUser().getId() == 0;
    }

    private boolean isPastTwentyFourHours(Comment comment) {
        final long DAY = 24 * 60 * 60 * 1000;
        return comment.getCreatedAt().getTime() > System.currentTimeMillis() - DAY;
    }
}