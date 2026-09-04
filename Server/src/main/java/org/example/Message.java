package org.example;

class Message{
    private final String author;
    private final String body;
    private final String timestamp;

    public Message(String author, String body, String timestamp) {
        this.author = author;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private String formatMessage(String msg) {
        return msg.trim();
    }

    public String getMessageForLog() {
        return timestamp + ": " + author + ": " + body;
    }
}
