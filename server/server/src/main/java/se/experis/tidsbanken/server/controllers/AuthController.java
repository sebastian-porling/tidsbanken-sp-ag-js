package se.experis.tidsbanken.server.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {
    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register(@RequestBody UserModel usermodel) {

    }
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody UserModel usermodel) {

    }
}
