package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.experis.tidsbanken.server.models.VacationRequest;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    VacationRequest getById(long id);

}
