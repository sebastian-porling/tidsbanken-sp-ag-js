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

    @GetMapping("/request/:request_id/comment")
    public ResponseEntity<CommonResponse> getComments(){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

       /*
        */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @PostMapping("/request/:request_id/comment")
    public ResponseEntity<CommonResponse> createComment(@RequestBody Comment comment) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @GetMapping("/request/:request_id/comment/{comment_id}")
    public ResponseEntity<CommonResponse> getComment(){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @PatchMapping("/request/:request_id/comment/{comment_id}")
    public ResponseEntity<CommonResponse> updateComment(@RequestBody Comment comment) {CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }

    @DeleteMapping("/request/:request_id/comment/{comment_id}")
    public ResponseEntity<CommonResponse> deleteComment(){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        /*
         */

        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }
}