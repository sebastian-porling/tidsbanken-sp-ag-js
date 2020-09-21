package se.experis.tidsbanken.server.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.CommentRepository;
import se.experis.tidsbanken.server.repositories.VacationRequestRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class CommentController{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VacationRequestRepository requestRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/request/{request_id}/comment")
    public ResponseEntity<CommonResponse> getComments(@PathVariable("request_id") Long requestId,
                                                      HttpServletRequest request){
        /* TODO - Owner and Admin should get */
        if(!authorizationService.isAuthorized(request)) { return unauthorized(); }
        try{
            Optional<VacationRequest> vacationRequestOp = requestRepository.findById(requestId);
            if (vacationRequestOp.isPresent()) {
                VacationRequest vr = vacationRequestOp.get();
                if (vr.getStatus().getStatus().equals("Approved")) {
                    return ResponseEntity.ok(new CommonResponse(
                            "All Comments For Request: " + vr.getId(), vr));
                } else return forbidden();
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("Not Founde"));
        } catch (Exception e) { return errorMessage(); }
    }

    @PostMapping("/request/{request_id}/comment")
    public ResponseEntity<CommonResponse> createComment(@PathVariable("request_id") Long requestId,
                                                        @RequestBody Comment comment,
                                                        HttpServletRequest request) {

        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<>(cr, resStatus);
    }

    @GetMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> getComment(@PathVariable("request_id") Long requestId,
                                                     @PathVariable("comment_id") Long commentId,
                                                     HttpServletRequest request){
        if(!authorizationService.isAuthorized(request)) { return unauthorized(); }

        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<>(cr, resStatus);
    }

    @PatchMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable("request_id") Long requestId,
                                                        @PathVariable("comment_id") Long commentId,
                                                        @RequestBody Comment comment,
                                                        HttpServletRequest request) {
        if(!authorizationService.isAuthorizedAdmin(request)) { return unauthorized(); }
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<>(cr, resStatus);

    }

    @DeleteMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable("request_id") Long requestId,
                                                        @PathVariable("comment_id") Long commentId,
                                                        HttpServletRequest request){
        if(!authorizationService.isAuthorized(request)) { return unauthorized(); }
        Optional<VacationRequest> vrOp = requestRepository.findById(requestId);
        if (vrOp.isPresent()) {
            Optional<Comment> commentOp = commentRepository.findByIdAndRequest(commentId, vrOp.get());
            if (commentOp.isPresent()) {
                final long DAY = 24 * 60 * 60 * 1000;
                if (commentOp.get().getCreatedAt().getTime() > System.currentTimeMillis() - DAY) {
                    commentRepository.delete(commentOp.get());
                    return ResponseEntity.ok(new CommonResponse("Deleted"));
                } else { return unauthorized(); }
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("Comment Not Found"));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("Vacation Request Not Found"));
    }

    private ResponseEntity<CommonResponse> errorMessage() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse(
                        "Something went wrong when trying to process the request "));
    }

    private ResponseEntity<CommonResponse> unauthorized() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponse( "Not Authorized"));
    }

    private ResponseEntity<CommonResponse> forbidden() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new CommonResponse("Forbidden"));
    }
}