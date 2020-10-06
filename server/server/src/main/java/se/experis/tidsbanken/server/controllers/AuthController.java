package se.experis.tidsbanken.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.HttpServerErrorException;
import se.experis.tidsbanken.server.DTOs.LoginDTO;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.services.LoginAttemptService;
import se.experis.tidsbanken.server.socket.SocketHandler;
import se.experis.tidsbanken.server.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private ResponseUtility responseUtility;
    @Autowired private TwoFactorAuth twoFactorAuth;
    @Autowired private LoginAttemptService loginAttemptService;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody Credentials credentials,
                                                HttpServletRequest request) {
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
                    } else { throw new Exception("Wrong code!"); }
                } else {
                    loginAttemptService.addFailedLoginAttempt(request);
                    return responseUtility.badRequest("Email or password is incorrect.");
                }
            } else { return responseUtility.badRequest("You need to provide both email and password to sign in"); }
        } catch (HttpServerErrorException.InternalServerError e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("Login authentication failed");
        } catch (Exception e) {
            return responseUtility.unauthorized();
        }
    }
}
