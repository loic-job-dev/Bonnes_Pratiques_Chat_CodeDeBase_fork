package org.example;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Server implements ServerInterface{
    private final String host;
    private final int serverPort;
    private List<ClientHandler> clientHandlerList = new ArrayList<>();
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private List<String> history = new ArrayList<>();

    public Server(String host, int port) {
        this.host = host;
        this.serverPort = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(host, serverPort));
        isRunning = true;
        System.out.println("Chat server started on port " + serverPort);

        while (isRunning) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket, this);
            clientHandlerList.add(clientHandler);
            Thread t = new Thread(clientHandler);
            t.start();
        }
    }

    public void stop() throws IOException {
        isRunning = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    @Override
    public String readHistory() throws IOException {
        StringBuilder completeHistory = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            completeHistory.append(history.get(i));
        }
        return completeHistory.toString();
    }

    @Override
    public void writeHistory(String message) throws IOException {
        history.add(message + " \n");
        if (history.size() > 100) {
            history.remove(0);
        }
    }

    @Override
    public void sendMessage(String message, UUID clientId) {
        for (int i = 0; i < clientHandlerList.size(); i++) {
            ClientHandler client = clientHandlerList.get(i);
            if (client.getClientId() != clientId) {
                try {
                    client.out.println(message);
                } catch (Exception e) {
                    // client déconnecté ?
                }
            }
        }
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        clientHandlerList.remove(clientHandler);
    }
}