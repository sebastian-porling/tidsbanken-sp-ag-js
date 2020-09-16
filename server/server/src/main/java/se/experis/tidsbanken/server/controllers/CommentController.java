package se.experis.tidsbanken.server.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController{
    @GetMapping("/request/:request_id/comment")
    public ResponseEntity<CommonResponse> getComments(){
    }

    @PostMapping("/request/:request_id/comment")
    public ResponseEntity<CommonResponse> createComment(@RequestBody Comment comment) {
        Comment createComment = commentRepository.save(comment);
        Response response = new Response(createComment, "CREATE");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/request/:request_id/comment/{comment_id}")
    public ResponseEntity<CommonResponse> getComments(){
    }

    @PatchMapping("/request/:request_id/comment/{comment_id}")
    public ResponseEntity<CommonResponse> updateComment(@RequestBody Comment comment) {
    }

    @DeleteMapping("/request/:request_id/comment/{comment_id}")
    public ResponseEntity<CommonResponse> deleteComment(){
    }
}