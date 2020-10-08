package se.experis.tidsbanken.server.socket;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents a memory store for socket.io clients
 */
@Component
public class SocketStore {
    final private HashMap<Long, SocketIOClient> clients = new HashMap<>();

    /**
     * Checks if the user has a client socket
     * @param userId Long representing a user's ID
     * @return true if the user have a client socket
     */
    public Optional<SocketIOClient> getUserClient(Long userId) {
        return Optional.ofNullable(clients.get(userId));
    }

    /**
     * Adds a client to the memory store
     * @param userId Which user the socket belongs to
     * @param client The user's client socket
     */
    public void addUserClient(Long userId, SocketIOClient client) {
        clients.put(userId, client);
    }

    /**
     * Removes the client socket by user id
     * @param userId Long representing a user's ID
     */
    public void removeUserClient(Long userId) {
        clients.remove(userId);
    }

}
