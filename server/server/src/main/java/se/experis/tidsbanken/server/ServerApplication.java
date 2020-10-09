package se.experis.tidsbanken.server;

import com.corundumstudio.socketio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Main program for starting the webserver
 */
@SpringBootApplication
public class ServerApplication {

	@Value("${server.host}")
	private String host;
	@Value("${server.ssl.key-store-password}")
	private String keyPassword;
	@Value("${socket.port}")
	private Integer port;

	/**
	 * Configures the socket.io server with ports and local hostname
	 * @return configured SocketIOServer instance
	 */
	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		try {
			System.out.println(host);
			System.out.println(keyPassword);
			System.out.println(port);
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
			e.printStackTrace();
		}
		return new SocketIOServer(config);
	}

	/**
	 * Main
	 * @param args no arguments used
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
