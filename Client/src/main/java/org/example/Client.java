package org.example;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {
    private final String serverAddress;
    private final int serverPort;
    private Socket socket;
    private ExecutorService executorService;
    private BufferedReader consoleReader;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void Connect() throws IOException, InterruptedException, ExecutionException {
        socket = new Socket(serverAddress, serverPort);
        executorService = Executors.newFixedThreadPool(2);

        Future<?> t1 = executorService.submit(this::receiveMessages);
        Thread.sleep(100);
        Future<?> t2 = executorService.submit(this::sendMessages);

        t1.get();
        t2.get();

        shutdown();
    }

    private void receiveMessages() {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                System.out.println("\r" + message);
                System.out.print("You: ");
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Disconnected");
        }
    }

    private void sendMessages() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String input;
            String author = null;
            while ((input = consoleReader.readLine()) != null) {
                bufferedWriter.write(input);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                if (author == null) {
                    author = input;
                }
                System.out.print("You: ");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void shutdown() throws IOException {
        if (executorService != null) {
            executorService.shutdown();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}