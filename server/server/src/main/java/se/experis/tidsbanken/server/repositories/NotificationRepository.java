package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.tidsbanken.server.models.Notification;
import se.experis.tidsbanken.server.models.User;

import java.util.List;

/**
 * Handles sql methods for Notifications
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Finds all notifications associated with user
     * @param user user
     * @return List of notifications or empty list
     */
    List<Notification> findAllByUser(User user);

    /**
     * Finds all notifications and orders them descended
     * @return List of all notifications
     */
    List<Notification> findAllByOrderByIdDesc();
}
