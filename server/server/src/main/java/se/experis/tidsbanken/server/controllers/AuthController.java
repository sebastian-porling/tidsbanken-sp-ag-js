package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register(@RequestBody AppUser user) {
        CommonResponse cr = new CommonResponse();
        cr.message = "Not Implemented";
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        return new ResponseEntity<>(cr, resStatus);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody AppUser user) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        if (user.email != null && user.password != null) {
            AppUser fetchedUser = userRepository.getByEmail(user.email);

            if(fetchedUser != null || fetchedUser.password.equals(user.password)) {
                cr.message = "User credentials approved";
                cr.status = 200;
                resStatus = HttpStatus.OK;
            } else {
                cr.message = "Failed authentication";
                cr.status = 401;
                resStatus = HttpStatus.UNAUTHORIZED;
            }
        } else {
            cr.message = "Failed authentication";
            cr.status = 401;
            resStatus = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(cr, resStatus);
    }
}
