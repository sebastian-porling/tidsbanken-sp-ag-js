package se.experis.tidsbanken.server.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.experis.tidsbanken.server.models.Notification;
import se.experis.tidsbanken.server.models.User;
import se.experis.tidsbanken.server.repositories.NotificationRepository;

import java.util.Optional;

/**
 * Observer for sending notifications to users
 */
@Component
public class NotificationObserver {

    @Autowired SocketStore socketStore;
    @Autowired NotificationRepository notificationRepository;

    /**
     * Adds a notification to the user in notification repository.
     * Sends the notification to the user if they have a client socket in socket store
     * @param message message to send
     * @param recipient user to receive notification
     */
    public void sendNotification(String message, User recipient) {
        if (message != null && recipient != null){
            final Notification notification = notificationRepository
                    .save(new Notification(message, recipient));
           socketStore.getUserClient(recipient.getId())
                   .ifPresent(ioClient -> ioClient.sendEvent("notification", notification));
        }
    }
}
