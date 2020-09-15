package se.experis.tidsbanken.server.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController{
    @GetMapping("/request/{request_id}/comment")
    public ResponseEntity<Comment> getComments(){
    }

    @PostMapping("/request/{request_id}/comment")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createComment = commentRepository.save(comment);
        Response response = new Response(createComment, "CREATE");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<Comment> getComments(){
    }

    @PatchMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
    }

    @DeleteMapping("/request/{request_id}/comment/{comment_id}")
    public ResponseEntity<Comment> deleteComment(){
    }
}