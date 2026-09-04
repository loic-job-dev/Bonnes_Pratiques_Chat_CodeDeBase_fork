package org.example.client;

import org.example.model.Message;
import org.example.model.User;
import org.example.network.ClientHandler;
import org.example.repository.MessageRepository;
import org.example.server.ClientManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ClientService {

    private static final int MAX_MESSAGE_LENGTH = 250;

    private final MessageRepository messageRepository;
    private final ClientManager clientManager;

    public ClientService(
            MessageRepository messageRepository,
            ClientManager clientManager
    ) {
        this.messageRepository = messageRepository;
        this.clientManager = clientManager;
    }

    public User createUser(String pseudo, UUID clientId) {
        if (pseudo == null || pseudo.isBlank()) {
            throw new IllegalArgumentException(
                    "Pseudo vide non autorisé."
            );
        }

        if (pseudo.length() > 15) {
            throw new IllegalArgumentException(
                    "Pseudo trop long."
            );
        }

        return new User(clientId, pseudo.trim());
    }

    public List<Message> getHistory() {
        return messageRepository.findAll();
    }

    public Message createMessage(User user, String body) {
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException(
                    "Message vide non autorisé."
            );
        }

        if (body.length() > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException(
                    "Message trop long. Maximum: "
                            + MAX_MESSAGE_LENGTH
                            + " caratères."
            );
        }

        return new Message(
                user,
                body.trim(),
                LocalDateTime.now()
        );
    }

    public void publishMessage(
            Message message,
            ClientHandler sender
    ) {
        messageRepository.save(message);

        String formattedMessage =
                message.getAuthor().getPseudo()
                        + ": "
                        + message.getBody();

        System.out.println(formattedMessage);

        clientManager.broadcast(
                formattedMessage,
                sender
        );
    }

    public void announceJoin(
            User user,
            ClientHandler sender
    ) {
        String message =
                user.getPseudo() + " a rejoint le serveur.";

        System.out.println(message);

        Message historyMessage = new Message(
                user,
                " a rejoint le serveur.",
                LocalDateTime.now()
        );

        messageRepository.save(historyMessage);

        clientManager.broadcast(message, sender);
    }

    public void announceLeave(
            User user,
            ClientHandler sender
    ) {
        String message =
                user.getPseudo() + " a quitté le serveur.";

        System.out.println(message);

        Message historyMessage = new Message(
                user,
                " a quitté le serveur.",
                LocalDateTime.now()
        );

        messageRepository.save(historyMessage);

        clientManager.broadcast(message, sender);

        clientManager.remove(sender);
    }
}