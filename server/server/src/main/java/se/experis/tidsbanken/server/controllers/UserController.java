package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;

import java.util.Optional;


@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<CommonResponse> getUser(){
    }


    @GetMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> getUser(@PathVariable("user_id") long user_id){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<AppUser> fetchedUser = userRepository.findById(user_id);

        if (fetchedUser != null){
            cr.data = fetchedUser;
            cr.message = "";
            cr.status = 200;
            resStatus = HttpStatus.OK;
        } else {
            cr.message = "";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(cr, resStatus);
    }

    @PatchMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> updateUser(@RequestBody AppUser user) {
    }

    @DeleteMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable("user_id")long user_id){
    }

    @GetMapping("/user/:user_id/requests")
    public ResponseEntity<CommonResponse> vacationRequests(@PathVariable("user_id")long user_id){
    }


    @PostMapping("/user/:user_id/update_password")
    public ResponseEntity<CommonResponse> updatePassword(@RequestBody AppUser user) {
    }


}
