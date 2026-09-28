package org.example.client;

import org.example.config.NetworkConfig;
import org.example.model.Message;
import org.example.model.User;
import org.example.network.ClientHandler;
import org.example.repository.MessageRepository;
import org.example.server.ClientManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientService {

    private final MessageRepository messageRepository;
    private final ClientManager clientManager;
    private final NetworkConfig networkConfig;
    private static final Logger logger =
            LoggerFactory.getLogger(ClientService.class);

    public ClientService(
            MessageRepository messageRepository,
            ClientManager clientManager,
            NetworkConfig networkConfig
    ) {
        this.messageRepository = messageRepository;
        this.clientManager = clientManager;
        this.networkConfig = networkConfig;
    }

    public User createUser(String pseudo, UUID clientId) {
        if (pseudo == null || pseudo.isBlank()) {
            logger.warn("Pseudo vide non autorisé.");
            throw new IllegalArgumentException(
                    "Pseudo vide non autorisé."
            );
        }

        if (pseudo.length() > networkConfig.maxPseudoLength()) {
            logger.warn(
                    "Pseudo trop long. Maximum: {} caractères.",
                    networkConfig.maxPseudoLength()
            );
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
            logger.warn("Message vide non autorisé.");
            throw new IllegalArgumentException(
                    "Message vide non autorisé."
            );
        }

        if (body.length() > networkConfig.maxMessageLength()) {
            logger.warn(
                    "Message trop long. Maximum: {} caractères.",
                    networkConfig.maxMessageLength()
            );
            throw new IllegalArgumentException(
                    "Message trop long. Maximum: "
                            + networkConfig.maxMessageLength()
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

        logger.info("{}", formattedMessage);

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

        logger.info("{}", message);

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

        logger.info("{}", message);

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