package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.services.ServerUserDetailsService;
import se.experis.tidsbanken.server.utils.Credentials;
import se.experis.tidsbanken.server.utils.JwtUtil;
import se.experis.tidsbanken.server.utils.UserRoll;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ServerUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register(@RequestBody AppUser user) {
        CommonResponse cr = new CommonResponse();
        cr.message = "Not Implemented";
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;
        return new ResponseEntity<>(cr, resStatus);
    }

//    @PostMapping("/login")
//    public ResponseEntity<CommonResponse> login(@RequestBody AppUser user) {
//
//        CommonResponse cr = new CommonResponse();
//        HttpStatus resStatus;
//
//        if (user.email != null && user.password != null) {
//            AppUser fetchedUser = userRepository.getByEmail(user.email);
//
//            if(fetchedUser != null || fetchedUser.password.equals(user.password)) {
//                cr.message = "User credentials approved";
//                cr.status = 200;
//                resStatus = HttpStatus.OK;
//            } else {
//                cr.message = "Failed authentication";
//                cr.status = 401;
//                resStatus = HttpStatus.UNAUTHORIZED;
//            }
//        } else {
//            cr.message = "Failed authentication";
//            cr.status = 401;
//            resStatus = HttpStatus.UNAUTHORIZED;
//        }
//
//        return new ResponseEntity<>(cr, resStatus);
//
//    }
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody Credentials credentials) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;
        try {
            if (credentials.getEmail() != null && credentials.getPassword() != null) {
                AppUser fetchedUser = userRepository.getByEmail(credentials.getEmail());
                if (fetchedUser != null || fetchedUser.password.equals(credentials.getPassword())) {
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    credentials.getEmail(), credentials.getPassword()));
                    final UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getEmail());
                    final UserRoll userRoll = fetchedUser.isAdmin ? UserRoll.ADMINISTRATOR : UserRoll.USER;
                    final String jwt = jwtUtil.generateToken(userDetails, userRoll.toString());
                    resStatus = HttpStatus.OK;
                    cr = new CommonResponse(resStatus.value(), "User credentials approved", jwt);
                } else {throw new BadCredentialsException("");}
            } else {throw new BadCredentialsException("");}

        } catch (BadCredentialsException e) {
            resStatus = HttpStatus.UNAUTHORIZED;
            cr = new CommonResponse(resStatus.value(), "Failed authentication");
        }
        return new ResponseEntity<>(cr, resStatus);
    }
}
