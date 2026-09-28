package org.example;

import org.example.config.NetworkConfig;
import org.example.repository.InMemoryMessageRepository;
import org.example.repository.MessageRepository;
import org.example.server.ClientManager;
import org.example.server.Server;
import org.example.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Logger logger = LoggerFactory.getLogger(Main.class);
        AppConfig appConfig = new AppConfig();
        NetworkConfig networkConfig = new NetworkConfig(appConfig);

        MessageRepository messageRepository =
                new InMemoryMessageRepository(networkConfig);

        ClientManager clientManager =
                new ClientManager();

        Server server = new Server(
                appConfig.getServerHost(),
                appConfig.getServerPort(),
                clientManager,
                messageRepository,
                networkConfig,
                logger
        );

        try {
            server.start();

        } catch (IOException e) {
            logger.error("Impossible de démarrer le serveur : ", e);

        } catch (RuntimeException e) {
            logger.error("Erreur inattendue : ", e);
        }
    }
}