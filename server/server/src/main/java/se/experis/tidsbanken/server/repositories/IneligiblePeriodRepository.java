package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.IneligiblePeriod;

@Repository
public interface IneligiblePeriodRepository extends JpaRepository<IneligiblePeriod, Long> {

}
