package se.experis.tidsbanken.server.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.experis.tidsbanken.server.models.User;
import se.experis.tidsbanken.server.models.VacationRequest;

import java.util.List;

/**
 * Handling sql methods for Vacation Requests
 */
@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    /**
     * Returns all Vacation Request by given owner
     * @param owner User
     * @return list of vacation requests
     */
    List<VacationRequest> findAllByOwner(User owner);

    /**
     * Returns all Vacation Requests on the given pageable sorted by descending order
     * @param pageable Pageable
     * @return List of vacation requests
     */
    List<VacationRequest> findAllByOrderByStartDesc(Pageable pageable);

    /**
     * Returns all vacation Requests except the given by id
     * @param owner User
     * @param id Vacation Request to not fetch
     * @return list of vacation requests owned by user
     */
    List<VacationRequest> findAllByOwnerAndIdNot(User owner, Long id);
}
