package org.example;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import org.joda.time.DateTime;

public class ClientHandler implements Runnable {
    private final Socket socket;
    PrintWriter out;
    private String pseudo;
    private final UUID clientId;
    private final ServerInterface server;

    public ClientHandler(Socket socket, ServerInterface server) {
        this.socket = socket;
        this.server = server;
        this.clientId = UUID.randomUUID();
    }

    public void run() {
        try {
            InputStream in = socket.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            OutputStream outStream = socket.getOutputStream();
            out = new PrintWriter(new OutputStreamWriter(outStream), true);

            out.println("Enter your name: ");
            pseudo = r.readLine();

            out.println(server.readHistory());

            String message = pseudo + " has joined the chat.";
            System.out.println(message);

            server.writeHistory(message);

            server.sendMessage(message, clientId);

            String receivedMessage;
            while ((receivedMessage = r.readLine()) != null) {
                message = pseudo + ": " + receivedMessage;
                System.out.println(message);

                server.writeHistory(message);

                server.sendMessage(message, clientId);

                Message messageToLog = new Message(pseudo, receivedMessage, new DateTime().toString());
                System.out.println(messageToLog.getMessageForLog());
            }

            String leavingMessage = pseudo + " has left the chat.";
            System.out.println(leavingMessage);

            server.writeHistory(leavingMessage);

            server.sendMessage(leavingMessage, clientId);

            server.removeClient(this);

        } catch (IOException e) {
            System.out.println("Client error");
        }
    }

    public UUID getClientId(){
        return this.clientId;
    }
}