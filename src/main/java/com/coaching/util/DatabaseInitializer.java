package com.coaching.util;

import com.coaching.exception.DatabaseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {
    private DatabaseInitializer() {
    }

    public static void initialize() {
        try {
            String schema = Files.readString(Paths.get("database/schema.sql"));
            String[] statements = schema.split(";");
            try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
                 Statement statement = pooled.unwrap().createStatement()) {
                for (String sql : statements) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty()) {
                        statement.execute(trimmed);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new DatabaseException("Failed to initialize database schema", e);
        }
    }
}
