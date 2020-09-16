package se.experis.tidsbanken.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/")
public class UserController {

    @GetMapping("/user")
    public ResponseEntity<CommonResponse> getUser(){
    }


    @GetMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> getProfile(@PathVariable("user_id")long user_id){
        Optional<CommonResponse> userProfile = userRepository.findById(user_id);
        if (userProfile.exists())
            return new ResponseEntity<>(userProfile.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> updateUser(@RequestBody UserModel usermodel) {
    }

    @DeleteMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable("user_id")long user_id){
    }

    @GetMapping("/user/:user_id/requests")
    public ResponseEntity<CommonResponse> vacationRequests(@PathVariable("user_id")long user_id){
    }


    @PostMapping("/user/:user_id/update_password")
    public ResponseEntity<CommonResponse> updatePassword(@RequestBody UserModel usermodel) {
    }


}
