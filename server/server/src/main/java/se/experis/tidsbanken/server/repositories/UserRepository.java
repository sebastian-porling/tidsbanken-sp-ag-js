package se.experis.tidsbanken.server.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> getByEmailAndIsActiveTrue(String email);

    Optional<User> findByIdAndIsActiveTrue(Long id);

    List<User> findAllByIsActiveTrue();

    List<User> findAllByIsAdminTrueAndIsActiveTrue();
}
