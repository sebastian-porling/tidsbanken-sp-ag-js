package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.tidsbanken.server.models.RequestStatus;

public interface StatusRepository extends JpaRepository<RequestStatus, Integer> {
    RequestStatus getById(int id);
}
