package org.example.network;


import org.example.client.ClientService;
import org.example.model.Message;
import org.example.model.User;
import org.example.utils.InputReader;
import org.example.utils.InputReader.InputTooLongException;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.UUID;

public class ClientHandler implements Runnable {

    private static final int SOCKET_TIMEOUT = 90_000;
    private static final int MAX_PSEUDO_LENGTH = 15;
    private static final int MAX_MESSAGE_LENGTH = 250;

    private final Socket socket;
    private final ClientService clientService;
    private final UUID clientId;

    private PrintWriter out;
    private InputReader inputReader;
    private User user;

    public ClientHandler(
            Socket socket,
            ClientService clientService
    ) {
        if (socket == null) {
            throw new IllegalArgumentException(
                    "Le socket ne peut pas être null."
            );
        }

        if (clientService == null) {
            throw new IllegalArgumentException(
                    "Le service client ne peut pas être null."
            );
        }

        this.socket = socket;
        this.clientService = clientService;
        this.clientId = UUID.randomUUID();
    }

    @Override
    public void run() {
        try {
            setupConnection();

            if (!authenticate()) {
                return;
            }

            sendHistory();

            clientService.announceJoin(user, this);

            handleMessages();

        } catch (SocketTimeoutException e) {
            System.out.println(
                    "Client déconnecté pour inactivité."
            );

        } catch (IOException e) {
            System.out.println(
                    "Erreur I/O avec le client : "
                            + e.getMessage()
            );

        } catch (NullPointerException e) {
            System.out.println(
                    user.getPseudo() + " a ragequit..."
            );
        }
        finally {
            disconnect();
        }
    }

    private void setupConnection() throws IOException {
        socket.setSoTimeout(SOCKET_TIMEOUT);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()),
                true
        );

        inputReader = new InputReader(reader);
    }

    private boolean authenticate() throws IOException {
        send("Entre ton pseudo: ");

        try {
            String pseudo =
                    inputReader.readLine(MAX_PSEUDO_LENGTH);

            if (pseudo == null) {
                return false;
            }

            user = clientService.createUser(
                    pseudo.trim(),
                    clientId
            );

            return true;

        } catch (InputTooLongException e) {
            send("Pseudo trop long.");
            return false;

        } catch (IllegalArgumentException e) {
            send(e.getMessage());
            return false;
        }
    }

    private void sendHistory() {
        List<Message> history =
                clientService.getHistory();

        for (Message message : history) {
            send(message.toString());
        }
    }

    private void handleMessages() throws IOException {
        String receivedMessage;

        while ((receivedMessage = readMessage()) != null) {

            if (receivedMessage.isBlank()) {
                continue;
            }

            try {
                Message message =
                        clientService.createMessage(
                                user,
                                receivedMessage
                        );

                clientService.publishMessage(
                        message,
                        this
                );

            } catch (IllegalArgumentException e) {
                send(e.getMessage());
            }
        }
    }

    private String readMessage() throws IOException {
        try {
            return inputReader
                    .readLine(MAX_MESSAGE_LENGTH)
                    .trim();

        } catch (InputTooLongException e) {
            send(
                    "Message trop long. Maximum: "
                            + MAX_MESSAGE_LENGTH
                            + " caractères."
            );

            return "";
        }
    }

    public void send(String message) {
        if (message == null || out == null || socket.isClosed()) {
            return;
        }

        out.println(message);
        out.flush();

        if (out.checkError()) {
            System.out.println(
                    "Erreur d'écriture vers le client."
            );
        }
    }

    private void disconnect() {
        if (user != null) {
            clientService.announceLeave(user, this);
        }

        closeConnection();
    }

    private void closeConnection() {
        if (out != null) {
            out.close();
        }

        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(
                    "Erreur lors de la fermeture : "
                            + e.getMessage()
            );
        }
    }

    public UUID getClientId() {
        return clientId;
    }
}