package org.example.utils;

public final class InputValidator {

    private InputValidator() {
    }

    public static final int MAX_MESSAGE_LENGTH = 500;

    public static String validateMessage(String message) {

        if (message == null) {
            return null;
        }

        String trimmedMessage = message.trim();

        if (trimmedMessage.isEmpty()) {
            return null;
        }

        if (trimmedMessage.length() > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException(
                    "Message too long. Maximum: "
                            + MAX_MESSAGE_LENGTH
                            + " characters."
            );
        }

        return trimmedMessage;
    }
}