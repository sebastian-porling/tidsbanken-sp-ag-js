package se.experis.tidsbanken.server.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.*;

import se.experis.tidsbanken.server.repositories.CommentRepository;

@Controller
public class CommentController{

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/request/{request_id}/comment")
    public ResponseEntity<CommonResponse> getComments(@PathVariable("request_id") Long requestId){

        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*
        */

        return new ResponseEntity<>(cr, resStatus);

    }

    @PostMapping("/request/{request_id}/comment")
    public ResponseEntity<CommonResponse> createComment(@PathVariable("request_id") Long requestId,
                                                        @RequestBody Comment comment) {

        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<>(cr, resStatus);
    }

    @GetMapping("/request/:{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> getComment(@PathVariable("request_id") Long requestId,
                                                     @PathVariable("comment_id") Long commentId){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<>(cr, resStatus);
    }

    @PatchMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable("request_id") Long requestId,
                                                        @PathVariable("comment_id") Long commentId,
                                                        @RequestBody Comment comment) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<>(cr, resStatus);

    }

    @DeleteMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable("request_id") Long requestId,
                                                        @PathVariable("comment_id") Long commentId){

        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<>(cr, resStatus);

    }
}