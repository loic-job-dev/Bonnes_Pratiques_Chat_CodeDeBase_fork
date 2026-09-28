package org.example.client;

import org.example.client.ClientService;
import org.example.config.NetworkConfig;
import org.example.model.Message;
import org.example.model.User;
import org.example.network.ClientHandler;
import org.example.repository.MessageRepository;
import org.example.server.ClientManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ClientManager clientManager;

    @Mock
    private NetworkConfig networkConfig;

    @Mock
    private ClientHandler clientHandler;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(
                messageRepository,
                clientManager,
                networkConfig
        );
    }

    @Test
    void createMessage_withEmptyBody_throwsException() {

        User user = new User(
                UUID.randomUUID(),
                "Bob"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> clientService.createMessage(user, "")
        );
    }

    @Test
    void createMessage_withTooLongBody_throwsException() {

        when(networkConfig.maxMessageLength())
                .thenReturn(250);

        User user = new User(
                UUID.randomUUID(),
                "Bob"
        );

        String body = "a".repeat(251);

        assertThrows(
                IllegalArgumentException.class,
                () -> clientService.createMessage(user, body)
        );
    }

    @Test
    void createMessage_withValidBody_returnsMessage() {

        when(networkConfig.maxMessageLength())
                .thenReturn(250);

        User user = new User(
                UUID.randomUUID(),
                "Bob"
        );

        Message message =
                clientService.createMessage(
                        user,
                        "Bonjour tout le monde"
                );

        assertEquals(
                "Bonjour tout le monde",
                message.getBody()
        );

        assertEquals(
                user,
                message.getAuthor()
        );
    }

    @Test
    void publishMessage_broadcastsFormattedMessage() {

        User user = new User(
                UUID.randomUUID(),
                "Bob"
        );

        Message message = new Message(
                user,
                "Bonjour",
                LocalDateTime.now()
        );

        clientService.publishMessage(
                message,
                clientHandler
        );

        verify(clientManager).broadcast(
                "Bob: Bonjour",
                clientHandler
        );
    }

    @Test
    void publishMessage_savesAndBroadcastsMessage() {

        User user = new User(
                UUID.randomUUID(),
                "Bob"
        );

        Message message = new Message(
                user,
                "Bonjour",
                LocalDateTime.now()
        );

        clientService.publishMessage(
                message,
                clientHandler
        );

        verify(messageRepository).save(message);

        verify(clientManager).broadcast(
                "Bob: Bonjour",
                clientHandler
        );
    }

    @Test
    void announceJoin_broadcastsJoinMessage() {

        User user = new User(
                UUID.randomUUID(),
                "Bob"
        );

        clientService.announceJoin(
                user,
                clientHandler
        );

        verify(clientManager).broadcast(
                "Bob a rejoint le serveur.",
                clientHandler
        );
    }

    @Test
    void announceLeave_broadcastsMessageAndRemovesClient() {

        User user = new User(
                UUID.randomUUID(),
                "Bob"
        );

        clientService.announceLeave(
                user,
                clientHandler
        );

        verify(messageRepository)
                .save(any(Message.class));

        verify(clientManager)
                .broadcast(
                        "Bob a quitté le serveur.",
                        clientHandler
                );

        verify(clientManager)
                .remove(clientHandler);
    }


}