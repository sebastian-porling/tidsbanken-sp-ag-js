package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser getById(long id);

    AppUser getByEmail (String email);

    AppUser findByIsActiveTrue();
}
