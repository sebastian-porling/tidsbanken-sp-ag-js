package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.utils.*;


import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody Credentials credentials) {
        try {
            if (credentials.getEmail() != null && credentials.getPassword() != null) {
               final Optional<User> fetchedUser = userRepository.getByEmail(credentials.getEmail());
                if (fetchedUser.isPresent() && fetchedUser.get().getPassword().equals(credentials.getPassword())) {
                    final User presentUser = fetchedUser.get();
                    final UserRole userRole = presentUser.isAdmin() ? UserRole.ADMINISTRATOR : UserRole.USER;
                    final String jwt = jwtUtil.generateToken(presentUser, userRole.toString());
                    return ResponseEntity.ok(new CommonResponse("User credentials approved", jwt));
                } else {throw new Exception("");}
            } else {throw new Exception("");}
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse("Failed authentication"));
        }
    }
}
