package se.experis.tidsbanken.server.socket;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
public class SocketStore {
    final private HashMap<Long, SocketIOClient> clients = new HashMap<>();

    public Optional<SocketIOClient> getUserClient(Long userId) {
        return Optional.of(clients.get(userId));
    }

    public void addUserClient(Long userId, SocketIOClient client) {
        clients.put(userId, client);
    }

    public void removeUserClient(Long userId) {
        clients.remove(userId);
    }

}
