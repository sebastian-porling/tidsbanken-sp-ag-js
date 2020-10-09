package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.experis.tidsbanken.server.models.LoginAttempt;
import se.experis.tidsbanken.server.repositories.LoginAttemptRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Handles rate-limiting for failed authentication attempts
 */
@Service
public class LoginAttemptService {

    @Autowired private LoginAttemptRepository loginAttemptRepository;

    /**
     * Checks if the user is banned and updates or removes the ban if it is expired or not
     * @param request Http request
     * @return true if they are not banned
     */
    public boolean checkFailedLoginAttempt(HttpServletRequest request) {
        final Optional<LoginAttempt> loginAttemptOp = loginAttemptRepository.findByIp(request.getRemoteAddr());
        if (loginAttemptOp.isPresent()) {
            final LoginAttempt loginAttempt = loginAttemptOp.get();
            if (checkMaxAttempts(loginAttempt)) {
                if (loginAttempt.getBlockedTimeStamp() == null) return false;
                if (checkIsBlocked(loginAttempt)) {
                    loginAttemptRepository.save(loginAttempt.setToBlocked());
                    return false;
                } else {
                    loginAttemptRepository.delete(loginAttempt);
                }
            }
        }
        return true;
    }

    /**
     * Adds a failed login attempt by user ip.
     * If the login attempt is ten or more set a ban timestamp
     * @param request Http Request
     */
    public void addFailedLoginAttempt(HttpServletRequest request) {
        final Optional<LoginAttempt> loginAttemptOp = loginAttemptRepository.findByIp(request.getRemoteAddr());
        if (loginAttemptOp.isPresent()) {
            final LoginAttempt loginAttempt = loginAttemptOp.get().incrementFailedAttempts();
            if (checkMaxAttempts(loginAttempt)) {
                loginAttemptRepository.save(loginAttempt.setToBlocked());
            } else {
                loginAttemptRepository.save(loginAttemptOp.get().incrementFailedAttempts());
            }
        } else {
            loginAttemptRepository.save(new LoginAttempt(request.getRemoteAddr()));
        }
    }

    /**
     * Checks if the user is blocked by the timestamp
     * @param loginAttempt Login Attempts
     * @return true if banned
     */
    private boolean checkIsBlocked(LoginAttempt loginAttempt) {
        return loginAttempt.getBlockedTimeStamp() != null && loginAttempt.getBlockedTimeStamp().after(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Checks if the login attempts has reach maximum of then tries
     * @param loginAttempt Login Attempts
     * @return true if exceeding or equal to 10
     */
    private boolean checkMaxAttempts(LoginAttempt loginAttempt) {
        return loginAttempt.getFailedAttempts() >= 10;
    }
}
