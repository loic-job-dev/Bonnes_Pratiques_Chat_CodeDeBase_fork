package org.example.network;

import org.example.model.ClientConfiguration;

import java.io.*;
import java.net.Socket;

public class SocketClient {

    private final ClientConfiguration configuration;

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public SocketClient(ClientConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(
                    "La configuration ne peut pas être null."
            );
        }

        this.configuration = configuration;
    }

    public void connect() throws IOException {

        socket = new Socket(
                configuration.getServerAddress(),
                configuration.getServerPort()
        );

        reader = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()
                )
        );

        writer = new BufferedWriter(
                new OutputStreamWriter(
                        socket.getOutputStream()
                )
        );

        System.out.println(
                "Connected to "
                        + configuration.getServerAddress()
                        + ":"
                        + configuration.getServerPort()
        );
    }

    public String receive() throws IOException {
        if (reader == null) {
            throw new IllegalStateException(
                    "Le client n'est pas connecté."
            );
        }

        return reader.readLine();
    }

    public void send(String message) throws IOException {

        if (writer == null) {
            throw new IllegalStateException(
                    "Le client n'est pas connecté."
            );
        }

        if (message == null) {
            return;
        }

        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public boolean isConnected() {
        return socket != null
                && !socket.isClosed();
    }

    public void disconnect() throws IOException {

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }

        reader = null;
        writer = null;
    }
}