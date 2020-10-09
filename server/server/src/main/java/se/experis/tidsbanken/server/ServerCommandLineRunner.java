package se.experis.tidsbanken.server;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Starts the Socket.io server
 */
@Component
public class ServerCommandLineRunner implements CommandLineRunner {

    @Autowired private SocketIOServer server;

    /**
     * Starts the socket.io server
     * @param args arguments are ignored
     * @throws Exception when socket server can't establish connection
     */
    @Override
    public void run(String... args) throws Exception {
        server.start();
    }
}