package org.example.model;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String pseudo;

    public User(UUID id, String pseudo) {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être null.");
        }

        if (pseudo == null || pseudo.isBlank()) {
            throw new IllegalArgumentException("Le pseudo ne peut pas être vide.");
        }

        this.id = id;
        this.pseudo = pseudo;
    }

    public UUID getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }
}