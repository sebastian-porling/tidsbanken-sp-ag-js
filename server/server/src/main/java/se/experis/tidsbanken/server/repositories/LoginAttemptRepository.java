package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.LoginAttempt;

import java.util.Optional;

/**
 * Handles sql methods for Login Attempts
 */
@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, String> {

    /**
     * Finds Login Attempt by ip
     * @param ip Interface Protocol Address
     * @return Optional with LoginAttempt or empty
     */
    Optional<LoginAttempt> findByIp(String ip);
}
