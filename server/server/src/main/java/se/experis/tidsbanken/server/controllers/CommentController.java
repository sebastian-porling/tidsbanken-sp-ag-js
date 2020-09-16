package se.experis.tidsbanken.server.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.models.Comment;
import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.repositories.CommentRepository;

@Controller
public class CommentController{

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/request/:request_id/comment")
    public ResponseEntity<CommonResponse> getComments(){
        return null;
    }

    @PostMapping("/request/:request_id/comment")
    public ResponseEntity<CommonResponse> createComment(@RequestBody Comment comment) {
        return null;
    }

    @GetMapping("/request/:request_id/comment/:comment_id")
    public ResponseEntity<CommonResponse> getComment(){
        return null;
    }

    @PatchMapping("/request/:request_id/comment/:comment_id")
    public ResponseEntity<CommonResponse> updateComment(@RequestBody Comment comment) {
        return null;
    }

    @DeleteMapping("/request/:request_id/comment/:comment_id")
    public ResponseEntity<CommonResponse> deleteComment(){
        return null;
    }
}