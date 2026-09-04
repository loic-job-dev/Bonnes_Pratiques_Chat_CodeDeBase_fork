package org.example;

import java.io.IOException;
import java.util.UUID;

public interface ServerInterface {
    public void start() throws IOException;

    public void stop() throws IOException;

    public String readHistory() throws IOException;

    public void writeHistory(String message) throws IOException;

    public void sendMessage(String message, UUID clientId);

    public void removeClient(ClientHandler clientHandler);
}
