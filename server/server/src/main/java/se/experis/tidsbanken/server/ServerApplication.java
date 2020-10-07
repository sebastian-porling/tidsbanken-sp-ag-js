package se.experis.tidsbanken.server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.InputStream;

@SpringBootApplication
public class ServerApplication {

	@Value("${server.host}")
	private String host;
	@Value("${server.ssl.key-store-password}")
	private String keyPassword;
	@Value("${socket.port}")
	private Integer port;

	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		try {

			config.setHostname(host);
			config.setPort(port);
			SocketConfig socketConfig = new SocketConfig();
			socketConfig.setReuseAddress(true);
			config.setKeyStorePassword(keyPassword);
			InputStream stream = null;
			stream = new FileInputStream("keystore.p12");
			config.setKeyStore(stream);
			config.setSocketConfig(socketConfig);
		} catch (Exception e) {
			System.out.println();
		}
		return new SocketIOServer(config);
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
