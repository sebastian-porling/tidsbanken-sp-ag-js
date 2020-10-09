package se.experis.tidsbanken.server.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Handles sql methods for User
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find user by email that is active
     * @param email User email
     * @return Optional with user or empty
     */
    Optional<User> getByEmailAndIsActiveTrue(String email);

    /**
     * Find user by email
     * @param email user email
     * @returnOptional with user or empty
     */
    Optional<User> getByEmail(String email);

    /**
     * Find user by user id and is active
     * @param id User id
     * @return Optional with user or empty
     */
    Optional<User> findByIdAndIsActiveTrue(Long id);

    /**
     * Find all active users
     * @return List of active users
     */
    List<User> findAllByIsActiveTrue();

    /**
     * Find all active admins
     * @return List of active admins
     */
    List<User> findAllByIsAdminTrueAndIsActiveTrue();
}
