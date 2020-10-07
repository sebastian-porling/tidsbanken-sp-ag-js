package se.experis.tidsbanken.server.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.experis.tidsbanken.server.models.User;
import se.experis.tidsbanken.server.repositories.NotificationRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;

@Component
public class SocketHandler {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private SocketStore socketStore;
    @Autowired private AuthorizationService authService;
    private Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    @Autowired
    public SocketHandler(SocketIOServer ioServer) {
        ioServer.addConnectListener(handleConnect());
        ioServer.addDisconnectListener(handleDisconnect());
        ioServer.addEventListener("all", Long.class, handleGetAll());
        ioServer.addEventListener("delete", Long.class, handleDelete());
        ioServer.addEventListener("deleteAll", Long.class, handleDeleteAll());
    }

    private ConnectListener handleConnect() {
        return socketIOClient -> {
            logger.info("Connecting client");
            final String token = parseToken(socketIOClient);
            if (authService.isAuthorized(token)) {
                final User user = authService.currentUser(token);
                socketStore.addUserClient(user.getId(), socketIOClient);
            } else {
                logger.warn("Unauthorized client");
            }
        };
    }

    private DisconnectListener handleDisconnect() {
        return socketIOClient -> {
            logger.info("Disconnecting client");
            final String token = parseToken(socketIOClient);
            if (authService.isAuthorized(token)) {
                final User user = authService.currentUser(token);
                socketStore.removeUserClient(user.getId());
            }
        };
    }

    private DataListener<Long> handleGetAll() {
        return (socketIOClient, __, ackRequest) -> {
            logger.info("Client receiving all user notifications");
            final String token = parseToken(socketIOClient);
            socketIOClient.sendEvent("notifications",
                    notificationRepository.findAllByUser(authService.currentUser(token)));
        };
    }

    private DataListener<Long> handleDelete() {
        return (socketIOClient, notificationId, ackRequest) -> {
            logger.info("Client deleting notification: " + notificationId);
            notificationRepository.findById(notificationId).ifPresent(n -> {
                notificationRepository.delete(n);
                socketIOClient.sendEvent("deleted", n.getId());
            });
        };
    }

    private DataListener<Long> handleDeleteAll() {
        return (socketIOClient, aLong, ackRequest) -> {
            logger.info("Client deleting all user notifications");
            final String token = parseToken(socketIOClient);
            notificationRepository.findAllByUser(authService.currentUser(token))
                    .forEach(notificationRepository::delete);
            socketIOClient.sendEvent("deletedAll");
        };
    }

    private String parseToken(SocketIOClient client) {
        return client.getHandshakeData().getSingleUrlParam("token");
    }
}
