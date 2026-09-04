package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String serverHost = "0.0.0.0";
        int serverPort = 12345;
        Server s = new Server(serverHost, serverPort);
        try {
            s.start();
        } catch (IOException e) {
            System.out.println("erreur");
        }
    }
}