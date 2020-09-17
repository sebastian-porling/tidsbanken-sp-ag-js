package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.experis.tidsbanken.server.models.AppUser;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.utils.JwtUtil;
import se.experis.tidsbanken.server.utils.UserRole;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Boolean isAuthorizedAdmin(HttpServletRequest request) {
        try {
            return isAuthorizedRole(request, UserRole.ADMINISTRATOR);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isAuthorizedUser(HttpServletRequest request) {
        try {
            return isAuthorizedRole(request, UserRole.USER);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isAuthorized(HttpServletRequest request) {
        try {
            String jwt = extractToken(request);
            String email = jwtUtil.extractEmail(jwt);
            AppUser user = userRepository.getByEmail(email).get();
            return jwtUtil.validateToken(jwt, user);
        } catch (Exception e) {
            return false;
        }
    }

    private Boolean isAuthorizedRole(HttpServletRequest request, UserRole userRole) throws Exception{
        String jwt = extractToken(request);
        String email = jwtUtil.extractEmail(jwt);
        AppUser user = userRepository.getByEmail(email).get();
        return jwtUtil.validateToken(jwt, user, userRole.toString());
    }

    private String extractToken(HttpServletRequest request) throws Exception{
        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }
        return jwt;
    }

    public AppUser currentUser(HttpServletRequest request) {
        try {
            String jwt = extractToken(request);
            String email = jwtUtil.extractEmail(jwt);
            return userRepository.getByEmail(email).get();
        } catch (Exception e) {
            return null;
        }
    }
}
