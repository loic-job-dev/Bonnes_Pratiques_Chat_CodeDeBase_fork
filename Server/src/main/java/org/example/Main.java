package org.example;

import org.example.repository.InMemoryMessageRepository;
import org.example.repository.MessageRepository;
import org.example.server.ClientManager;
import org.example.server.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        String serverHost = "0.0.0.0";
        int serverPort = 12345;

        MessageRepository messageRepository =
                new InMemoryMessageRepository();

        ClientManager clientManager =
                new ClientManager();

        Server server = new Server(
                serverHost,
                serverPort,
                clientManager,
                messageRepository
        );

        try {
            server.start();

        } catch (IOException e) {
            System.err.println(
                    "Impossible de démarrer le serveur : "
                            + e.getMessage()
            );

        } catch (RuntimeException e) {
            System.err.println(
                    "Erreur inattendue : "
                            + e.getMessage()
            );
        }
    }
}