package org.example.config;

public class NetworkConfig {

    private final int socketTimeout;
    private final int maxPseudoLength;
    private final int maxMessageLength;
    private final int maxHistorySize;

    public NetworkConfig(AppConfig config) {
        this.socketTimeout = Integer.parseInt(config.get("socketTimeout"));
        this.maxPseudoLength = Integer.parseInt(config.get("maxPseudoLength"));
        this.maxMessageLength = Integer.parseInt(config.get("maxMessageLength"));
        this.maxHistorySize = Integer.parseInt(config.get("maxHistorySize"));
    }

    public int socketTimeout() {
        return socketTimeout;
    }

    public int maxPseudoLength() {
        return maxPseudoLength;
    }

    public int maxMessageLength() {
        return maxMessageLength;
    }

    public int getMaxHistorySize() {
        return maxHistorySize;
    }
}