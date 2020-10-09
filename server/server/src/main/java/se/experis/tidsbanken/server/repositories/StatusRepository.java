package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.Status;

import java.util.Optional;

/**
 * Handles sql methods for Vacation Status
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

    /**
     * Find status by status text
     * @param status status text
     * @return Optional with status or empty
     */
    Optional<Status> findByStatus(String status);
}
