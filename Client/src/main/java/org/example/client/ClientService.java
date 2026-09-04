package org.example.client;

import org.example.network.SocketClient;
import org.example.utils.InputValidator;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientService {

    private final SocketClient socketClient;

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(2);

    private BufferedReader consoleReader;

    public ClientService(SocketClient socketClient) {
        if (socketClient == null) {
            throw new IllegalArgumentException(
                    "Le client réseau ne peut pas être null."
            );
        }

        this.socketClient = socketClient;
    }

    public void start()
            throws IOException,
            InterruptedException {

        socketClient.connect();

        consoleReader = new BufferedReader(
                new InputStreamReader(System.in)
        );

        Future<?> receiveTask =
                executorService.submit(
                        this::receiveMessages
                );

        Future<?> sendTask =
                executorService.submit(
                        this::sendMessages
                );

        try {
            receiveTask.get();
            sendTask.get();

        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException(
                    "Erreur lors de l'exécution du client.",
                    e
            );

        } finally {
            shutdown();
        }
    }

    private void receiveMessages() {
        try {
            String message;

            while ((message = socketClient.receive()) != null) {
                displayMessage(message);
            }

        } catch (IOException e) {
            System.out.println(
                    "Déconnecté: " + e.getMessage()
            );
        }
    }

    private void sendMessages() {

        try {
            String input;

            while ((input = consoleReader.readLine()) != null) {

                try {
                    String validatedMessage =
                            InputValidator.validateMessage(input);

                    if (validatedMessage == null) {
                        continue;
                    }

                    socketClient.send(validatedMessage);

                    System.out.print("Toi: ");

                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.print("Toi: ");
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Erreur pendant l'envoi du message: "
                            + e.getMessage()
            );
        }
    }

    private void displayMessage(String message) {

        if (message == null) {
            return;
        }

        System.out.println("\r" + message);
        System.out.print("Toi: ");
    }

    private void shutdown() throws IOException {

        executorService.shutdown();

        if (consoleReader != null) {
            consoleReader.close();
        }

        socketClient.disconnect();
    }
}