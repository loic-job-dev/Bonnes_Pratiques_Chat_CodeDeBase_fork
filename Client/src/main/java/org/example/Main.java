package org.example;

import org.example.client.ClientService;
import org.example.model.ClientConfiguration;
import org.example.network.SocketClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        String serverAddress = "localhost";
        int serverPort = 12345;

        ClientConfiguration configuration =
                new ClientConfiguration(
                        serverAddress,
                        serverPort
                );

        SocketClient socketClient =
                new SocketClient(configuration);

        ClientService clientService =
                new ClientService(socketClient);

        try {
            clientService.start();

        } catch (IOException e) {
            System.err.println(
                    "Erreur réseau : "
                            + e.getMessage()
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            System.err.println(
                    "Le client a été interrompu."
            );
        }
    }
}