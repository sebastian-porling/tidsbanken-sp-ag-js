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

/**
 * For handling all socket functionallity
 */
@Component
public class SocketHandler {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private SocketStore socketStore;
    @Autowired private AuthorizationService authService;
    private Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    /**
     * Setup for SocketIOServer "endpoints"
     * @param ioServer socnet.io server
     */
    @Autowired
    public SocketHandler(SocketIOServer ioServer) {
        ioServer.addConnectListener(handleConnect());
        ioServer.addDisconnectListener(handleDisconnect());
        ioServer.addEventListener("all", Long.class, handleGetAll());
        ioServer.addEventListener("delete", Long.class, handleDelete());
        ioServer.addEventListener("deleteAll", Long.class, handleDeleteAll());
    }

    /**
     * Handle client socket connection.
     * Adds the user to the local store of authorized
     * @return lambda function for the method
     */
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

    /**
     * Handle client socket disconnect.
     * Removes user from the local store
     * @return lambda function for the method
     */
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

    /**
     * Handle getting all notifications for the socket user
     * @return lambda function for the method
     */
    private DataListener<Long> handleGetAll() {
        return (socketIOClient, __, ackRequest) -> {
            logger.info("Client receiving all user notifications");
            final String token = parseToken(socketIOClient);
            socketIOClient.sendEvent("notifications",
                    notificationRepository.findAllByUser(authService.currentUser(token)));
        };
    }

    /**
     * Handles deleting of user notifications
     * @return lambda function for the method
     */
    private DataListener<Long> handleDelete() {
        return (socketIOClient, notificationId, ackRequest) -> {
            logger.info("Client deleting notification: " + notificationId);
            notificationRepository.findById(notificationId).ifPresent(n -> {
                notificationRepository.delete(n);
                socketIOClient.sendEvent("deleted", n.getId());
            });
        };
    }

    /**
     * Handles deleting all user notifications
     * @return lambda function for the method
     */
    private DataListener<Long> handleDeleteAll() {
        return (socketIOClient, aLong, ackRequest) -> {
            logger.info("Client deleting all user notifications");
            final String token = parseToken(socketIOClient);
            notificationRepository.findAllByUser(authService.currentUser(token))
                    .forEach(notificationRepository::delete);
            socketIOClient.sendEvent("deletedAll");
        };
    }

    /**
     * Parses the client socket handshake for JWT token
     * @param client socket.io client
     * @return token if it exists.
     */
    private String parseToken(SocketIOClient client) {
        return client.getHandshakeData().getSingleUrlParam("token");
    }
}
