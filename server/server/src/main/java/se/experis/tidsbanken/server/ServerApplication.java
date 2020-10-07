package se.experis.tidsbanken.server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {

	private String host = "172.31.28.73";
	private Integer port = 4121;

	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port);
		SocketConfig socketConfig = new SocketConfig();
		socketConfig.setReuseAddress(true);
		config.setSocketConfig(socketConfig);
		return new SocketIOServer(config);
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
