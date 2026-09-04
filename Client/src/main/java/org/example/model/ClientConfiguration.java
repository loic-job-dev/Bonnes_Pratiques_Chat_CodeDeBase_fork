package org.example.model;

public class ClientConfiguration {

    private final String serverAddress;
    private final int serverPort;

    public ClientConfiguration(
            String serverAddress,
            int serverPort
    ) {
        if (serverAddress == null || serverAddress.isBlank()) {
            throw new IllegalArgumentException(
                    "L'adresse du serveur est invalide."
            );
        }

        if (serverPort < 1 || serverPort > 65535) {
            throw new IllegalArgumentException(
                    "Le port doit être compris entre 1 et 65535."
            );
        }

        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }
}