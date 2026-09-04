package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class InputReader {

    private final BufferedReader reader;

    public InputReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String readLine(int maxLength)
            throws IOException, InputTooLongException {

        StringBuilder result = new StringBuilder();

        int character;

        while ((character = reader.read()) != -1) {

            if (character == '\n') {
                break;
            }

            if (character == '\r') {
                continue;
            }

            result.append((char) character);

            if (result.length() > maxLength) {
                throw new InputTooLongException();
            }
        }

        if (character == -1 && result.isEmpty()) {
            return null;
        }

        return result.toString();
    }

    public static class InputTooLongException extends Exception {

        public InputTooLongException() {
            super("Texte trop long");
        }
    }
}