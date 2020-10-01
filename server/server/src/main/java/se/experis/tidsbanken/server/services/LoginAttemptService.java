package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.experis.tidsbanken.server.models.LoginAttempt;
import se.experis.tidsbanken.server.repositories.LoginAttemptRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class LoginAttemptService {

    @Autowired private LoginAttemptRepository loginAttemptRepository;

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

    private boolean checkIsBlocked(LoginAttempt loginAttempt) {
        return loginAttempt.getBlockedTimeStamp() != null && loginAttempt.getBlockedTimeStamp().after(new Timestamp(System.currentTimeMillis()));
    }

    private boolean checkMaxAttempts(LoginAttempt loginAttempt) {
        return loginAttempt.getFailedAttempts() >= 10;
    }
}
