package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

}
