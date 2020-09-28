package se.experis.tidsbanken.server.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.experis.tidsbanken.server.models.Notification;
import se.experis.tidsbanken.server.models.User;
import se.experis.tidsbanken.server.repositories.NotificationRepository;

import java.util.Optional;

@Component
public class NotificationObserver {

    @Autowired SocketStore socketStore;

    @Autowired NotificationRepository notificationRepository;

    public void sendNotification(String message, User recipient) {
        if (message != null && recipient != null){
            final Notification notification = notificationRepository
                    .save(new Notification(message, recipient));
           socketStore.getUserClient(recipient.getId())
                   .ifPresent(ioClient -> ioClient.sendEvent("notification", notification));
        }
    }
}
