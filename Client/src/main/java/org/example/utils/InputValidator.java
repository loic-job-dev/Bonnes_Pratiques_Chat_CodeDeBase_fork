package org.example.utils;

import org.example.config.NetworkConfig;

public final class InputValidator {

    private final NetworkConfig networkConfig;

    public InputValidator(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public String validateMessage(String message) {

        if (message == null) {
            return null;
        }

        String trimmedMessage = message.trim();

        if (trimmedMessage.isEmpty()) {
            return null;
        }

        if (trimmedMessage.length() > networkConfig.maxMessageLength()) {
            throw new IllegalArgumentException(
                    "Message trop long. Maximum: "
                            + networkConfig.maxMessageLength()
                            + " caractères."
            );
        }

        return trimmedMessage;
    }
}