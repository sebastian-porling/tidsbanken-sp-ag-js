package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.tidsbanken.server.models.VacationRequest;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    VacationRequest getById(long id);
}
