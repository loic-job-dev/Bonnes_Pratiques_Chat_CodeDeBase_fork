package org.example.config;

import java.io.IOException;
import java.util.Properties;

public class AppConfig {

    private final Properties properties = new Properties();

    public AppConfig() {
        try (var input = AppConfig.class.getClassLoader()
                .getResourceAsStream("app.properties")) {

            if (input == null) {
                throw new RuntimeException("app.properties introuvable");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger la configuration", e);
        }
    }

    public String getVersion() {
        return properties.getProperty("version");
    }

    public String getServerAddress() {
        return properties.getProperty("serverAddress");
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("serverPort"));
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
