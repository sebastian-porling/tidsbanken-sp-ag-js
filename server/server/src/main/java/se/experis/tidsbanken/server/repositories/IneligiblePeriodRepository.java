package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.IneligiblePeriod;

import java.util.List;

@Repository
public interface IneligiblePeriodRepository extends JpaRepository<IneligiblePeriod, Long> {

    List<IneligiblePeriod> findAllByOrderByStartDesc();
}
