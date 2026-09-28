package org.example.repository;

import org.example.config.NetworkConfig;
import org.example.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryMessageRepository implements MessageRepository {

    private final NetworkConfig networkConfig;

    public InMemoryMessageRepository(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    private final List<Message> messages =
            Collections.synchronizedList(new ArrayList<>());

    @Override
    public void save(Message message) {
        messages.add(message);

        if (messages.size() > networkConfig.getMaxHistorySize()) {
            messages.remove(0);
        }
    }

    @Override
    public List<Message> findAll() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }
}