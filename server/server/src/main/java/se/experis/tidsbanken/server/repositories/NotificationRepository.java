package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.tidsbanken.server.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
