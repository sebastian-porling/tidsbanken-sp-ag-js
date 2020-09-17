package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.utils.Credentials;
import se.experis.tidsbanken.server.utils.JwtUtil;
import se.experis.tidsbanken.server.utils.UserRole;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register(@RequestBody AppUser user) {
        CommonResponse cr = new CommonResponse();
        cr.message = "Not Implemented";
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;
        return new ResponseEntity<>(cr, resStatus);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody Credentials credentials) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;
        try {
            if (credentials.getEmail() != null && credentials.getPassword() != null) {
                Optional<AppUser> fetchedUser = userRepository.getByEmail(credentials.getEmail());
                if (fetchedUser.isPresent() && fetchedUser.get().password.equals(credentials.getPassword())) {
                    AppUser presentUser = fetchedUser.get();
                    final UserRole userRole = presentUser.isAdmin ? UserRole.ADMINISTRATOR : UserRole.USER;
                    final String jwt = jwtUtil.generateToken(presentUser, userRole.toString());
                    resStatus = HttpStatus.OK;
                    cr = new CommonResponse(resStatus.value(), "User credentials approved", jwt);
                } else {throw new BadCredentialsException("");}
            } else {throw new BadCredentialsException("");}
        } catch (Exception e) {
            resStatus = HttpStatus.UNAUTHORIZED;
            cr = new CommonResponse(resStatus.value(), "Failed authentication");
        }
        return new ResponseEntity<>(cr, resStatus);
    }
}
