package se.experis.tidsbanken.server.repositories;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getById(int id);

    User getByUsername (String username);

    User findByIsActiveTrue();
}
