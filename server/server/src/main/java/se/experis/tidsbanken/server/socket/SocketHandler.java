package se.experis.tidsbanken.server.socket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.experis.tidsbanken.server.repositories.NotificationRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;

@Component
public class SocketHandler {

    @Autowired private NotificationRepository notificationRepository;

    @Autowired private SocketStore socketStore;

    @Autowired private AuthorizationService authService;

    @Autowired
    public SocketHandler(SocketIOServer ioServer) {

        ioServer.addConnectListener(handleConnect());
        ioServer.addDisconnectListener(handleDisconnect());
        ioServer.addEventListener("get all", Long.class, handleGetAll());
        ioServer.addEventListener("mark read", Long.class, handleMarkRead());
        ioServer.addEventListener("delete", Long.class, handleDelete());
    }

    private ConnectListener handleConnect() {
        return socketIOClient -> {
            System.out.println("Connecting");
            HandshakeData handshakeData = socketIOClient.getHandshakeData();
            HttpHeaders headers = handshakeData.getHttpHeaders();
            System.out.println(headers.toString());
        };
    }

    private DisconnectListener handleDisconnect() {
        return socketIOClient -> {
            System.out.println("Disconnecting");
            HttpHeaders headers = socketIOClient.getHandshakeData().getHttpHeaders();
            System.out.println(headers.toString());
        };
    }

    private DataListener<Long> handleGetAll() {
        return (socketIOClient, __, ackRequest) -> {
            System.out.println("handle get all");
            socketIOClient.sendEvent("notification",
                    notificationRepository.findAll());
        };
    }

    private DataListener<Long> handleMarkRead() {
        return (socketIOClient, notificationId, ackRequest) -> {
            System.out.println("handle mark read");

            socketIOClient.sendEvent("read", notificationId);
        };
    }

    private DataListener<Long> handleDelete() {
        return (socketIOClient, notificationId, ackRequest) -> {
            System.out.println("deleting");
            socketIOClient.sendEvent("delete", notificationId);
        };
    }
}
