package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.tidsbanken.server.models.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    AppUser getById(int id);

    AppUser getByEmail (String email);

    AppUser findByIsActiveTrue();
}
