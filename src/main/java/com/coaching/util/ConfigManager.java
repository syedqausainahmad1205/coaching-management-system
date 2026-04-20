package com.coaching.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config/db.properties")) {
            PROPERTIES.load(fis);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config/db.properties", e);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
