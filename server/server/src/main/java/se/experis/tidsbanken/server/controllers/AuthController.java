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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired private UserRepository userRepository;

    @Autowired private JwtUtil jwtUtil;

    @Autowired private ResponseUtility responseUtility;

    @Autowired private TwoFactorAuth twoFactorAuth;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody Credentials credentials) {
        try {
            if (credentials.getEmail() != null && credentials.getPassword() != null) {
                final Optional<User> fetchedUser = userRepository.getByEmailAndIsActiveTrue(credentials.getEmail());
                if (fetchedUser.isPresent() && fetchedUser.get().getPassword().equals(credentials.getPassword())) {
                    final User presentUser = fetchedUser.get();
                    if(!presentUser.isTwoFactorAuth()) {
                        presentUser.setTwoFactorAuth(true);
                        final String secret = presentUser.generateSecret();
                        userRepository.save(presentUser);
                        return responseUtility.ok("Register Two-Factor-Authentication",
                                twoFactorAuth.generateQrAuth(secret, presentUser.getEmail()));
                    } else if(credentials.getCode() != null && twoFactorAuth.verifyCode(presentUser.getSecret(), credentials.getCode())){
                        final UserRole userRole = presentUser.isAdmin() ? UserRole.ADMINISTRATOR : UserRole.USER;
                        final String jwt = jwtUtil.generateToken(presentUser, userRole.toString());
                        return responseUtility.ok("User credentials approved", new LoginDTO(jwt,presentUser));
                    } else { throw new Exception(""); }
                } else { throw new Exception(""); }
            } else { throw new Exception(""); }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse().message("Failed authentication"));
        }
    }
}
