package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.IneligiblePeriod;

import java.util.List;

/**
 * Handles sql methods for Ineligible Periods
 */
@Repository
public interface IneligiblePeriodRepository extends JpaRepository<IneligiblePeriod, Long> {

    /**
     * Finds all ineligible periods and orders them by descending order of start period
     * @return List of ineligible periods
     */
    List<IneligiblePeriod> findAllByOrderByStartDesc();

    /**
     * Finds all ineligible periods except the one provided
     * @param id Ineligible period id
     * @return list of ineligible periods
     */
    List<IneligiblePeriod> findAllByIdNot(Long id);
}
