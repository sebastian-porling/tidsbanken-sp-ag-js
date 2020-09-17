package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.experis.tidsbanken.server.models.AppUser;
import se.experis.tidsbanken.server.models.VacationRequest;

import java.util.List;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    List<VacationRequest> findAllByOwner(AppUser owner);

}
