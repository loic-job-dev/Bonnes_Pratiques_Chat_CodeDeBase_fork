package org.example;

import org.example.client.ClientService;
import org.example.config.AppConfig;
import org.example.config.NetworkConfig;
import org.example.model.ClientConfiguration;
import org.example.network.SocketClient;
import org.example.utils.InputValidator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        AppConfig appConfig = new AppConfig();
        NetworkConfig networkConfig = new NetworkConfig(appConfig);
        InputValidator inputValidator = new InputValidator(networkConfig);

        ClientConfiguration configuration =
                new ClientConfiguration(
                        appConfig.getServerAddress(),
                        appConfig.getServerPort()
                );

        SocketClient socketClient =
                new SocketClient(configuration);

        ClientService clientService =
                new ClientService(socketClient, inputValidator);

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