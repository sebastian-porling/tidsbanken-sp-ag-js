package se.experis.tidsbanken.server.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.AppUser;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> getByEmail(String email);

}
