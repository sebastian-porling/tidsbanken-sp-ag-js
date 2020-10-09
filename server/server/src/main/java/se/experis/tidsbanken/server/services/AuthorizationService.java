package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.experis.tidsbanken.server.models.User;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.utils.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles authorization with JWT tokens
 */
@Service
public class AuthorizationService {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;

    /**
     * Checks if the request user is a authorized admin
     * @param request Http Request
     * @return True of admin
     */
    public Boolean isAuthorizedAdmin(HttpServletRequest request) {
        try { return isAuthorizedRole(request, UserRole.ADMINISTRATOR);
        } catch (Exception e) { return false; }
    }

    /**
     * Checks if the user is a authorized user
     * @param request Http Request
     * @return True if a user
     */
    public Boolean isAuthorizedUser(HttpServletRequest request) {
        try { return isAuthorizedRole(request, UserRole.USER);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the current request is authorized
     * @param request Http Request
     * @return true if authorized
     */
    public Boolean isAuthorized(HttpServletRequest request) {
        try {
            final String jwt = extractToken(request);
            return isAuthorized(jwt);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the jwt token is authorized
     * @param jwt String JWT
     * @return true if authorized
     */
    public Boolean isAuthorized(String jwt) {
        try {
            final Long userId = jwtUtil.extractUserId(jwt);
            final User user = userRepository.findByIdAndIsActiveTrue(userId).get();
            return jwtUtil.validateToken(jwt, user);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the request is authorized by given role
     * @param request Http Request
     * @param userRole User Role Type
     * @return True if authorized by given role
     * @throws Exception Throws exception if server error
     */
    private Boolean isAuthorizedRole(HttpServletRequest request, UserRole userRole) throws Exception{
        try{
            final String jwt = extractToken(request);
            final Long userId = jwtUtil.extractUserId(jwt);
            final User user = userRepository.findByIdAndIsActiveTrue(userId).get();
            return jwtUtil.validateToken(jwt, user, userRole.toString());
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the JWT token from the http request authorization header
     * @param request Http Request
     * @return String JWT token or null if no token
     * @throws Exception
     */
    private String extractToken(HttpServletRequest request) throws Exception{
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            return authorizationHeader.substring(7);
        return null;
    }

    /**
     * Returns the user of the current Http Request
     * @param request Http Request
     * @return User
     */
    public User currentUser(HttpServletRequest request) {
        try {
            final String jwt = extractToken(request);
            return currentUser(jwt);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the user of the JWT Token
     * @param jwt String JWT
     * @return User
     */
    public User currentUser(String jwt) {
        try {
            final Long userId = jwtUtil.extractUserId(jwt);
            return userRepository.findByIdAndIsActiveTrue(userId).get();
        } catch (Exception e) {
            return null;
        }
    }
}
