package org.example.model;

import java.time.LocalDateTime;

public class Message {

    private final User author;
    private final String body;
    private final LocalDateTime timestamp;

    public Message(User author, String body, LocalDateTime timestamp) {
        if (author == null) {
            throw new IllegalArgumentException("L'auteur ne peut pas être null.");
        }

        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Le message ne peut pas être vide.");
        }

        this.author = author;
        this.body = body;
        this.timestamp = timestamp;
    }

    public User getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessageForLog() {
        return timestamp + ": "
                + author.getId()
                + " ("
                + author.getPseudo()
                + "): "
                + body;
    }

    @Override
    public String toString() {
        return author.getPseudo() + ": " + body;
    }
}