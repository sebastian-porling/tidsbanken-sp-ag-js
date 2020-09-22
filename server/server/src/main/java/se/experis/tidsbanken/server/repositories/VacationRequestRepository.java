package se.experis.tidsbanken.server.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.experis.tidsbanken.server.models.User;
import se.experis.tidsbanken.server.models.VacationRequest;

import java.util.List;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    List<VacationRequest> findAllByOwner(User owner);

    List<VacationRequest> findAllByOrderByStartDesc(Pageable pageable);

    List<VacationRequest> findAllByOwnerAndIdNot(User owner, Long id);
}
