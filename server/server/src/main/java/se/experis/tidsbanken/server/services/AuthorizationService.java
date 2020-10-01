package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.experis.tidsbanken.server.models.User;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.utils.*;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Boolean isAuthorizedAdmin(HttpServletRequest request) {
        try { return isAuthorizedRole(request, UserRole.ADMINISTRATOR);
        } catch (Exception e) { return false; }
    }

    public Boolean isAuthorizedUser(HttpServletRequest request) {
        try { return isAuthorizedRole(request, UserRole.USER);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isAuthorized(HttpServletRequest request) {
        try {
            final String jwt = extractToken(request);
            return isAuthorized(jwt);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isAuthorized(String jwt) {
        try {
            final Long userId = jwtUtil.extractUserId(jwt);
            final User user = userRepository.findByIdAndIsActiveTrue(userId).get();
            return jwtUtil.validateToken(jwt, user);
        } catch (Exception e) {
            return false;
        }
    }

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

    private String extractToken(HttpServletRequest request) throws Exception{
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            return authorizationHeader.substring(7);
        return null;
    }

    public User currentUser(HttpServletRequest request) {
        try {
            final String jwt = extractToken(request);
            return currentUser(jwt);
        } catch (Exception e) {
            return null;
        }
    }

    public User currentUser(String jwt) {
        try {
            final Long userId = jwtUtil.extractUserId(jwt);
            return userRepository.findByIdAndIsActiveTrue(userId).get();
        } catch (Exception e) {
            return null;
        }
    }
}
