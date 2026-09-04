package org.example.server;

import org.example.client.ClientService;
import org.example.network.ClientHandler;
import org.example.repository.MessageRepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final String host;
    private final int port;
    private final ClientManager clientManager;
    private final MessageRepository messageRepository;

    private ServerSocket serverSocket;
    private boolean running;

    public Server(
            String host,
            int port,
            ClientManager clientManager,
            MessageRepository messageRepository
    ) {
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException(
                    "L'adresse du serveur est invalide."
            );
        }

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException(
                    "Le port doit être compris entre 1 et 65535."
            );
        }

        this.host = host;
        this.port = port;
        this.clientManager = clientManager;
        this.messageRepository = messageRepository;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(host, port));

        running = true;

        System.out.println(
                "Serveur démarré sur le port " + port
        );

        while (running) {
            Socket socket = serverSocket.accept();

            ClientService clientService = new ClientService(
                    messageRepository,
                    clientManager
            );

            ClientHandler clientHandler = new ClientHandler(
                    socket,
                    clientService
            );

            clientManager.add(clientHandler);

            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    }

    public void stop() throws IOException {
        running = false;

        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}