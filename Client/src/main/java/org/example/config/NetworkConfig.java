package org.example.config;

public class NetworkConfig {

    private final int maxMessageLength;

    public NetworkConfig(AppConfig config) {
        this.maxMessageLength = Integer.parseInt(config.get("maxMessageLength"));
    }

    public int maxMessageLength() {
        return maxMessageLength;
    }
}