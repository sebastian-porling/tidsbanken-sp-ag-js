package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.tidsbanken.server.models.IneligiblePeriod;

public interface IneligiblePeriodRepository extends JpaRepository<IneligiblePeriod, Long> {
    IneligiblePeriod getById(long id);
}
