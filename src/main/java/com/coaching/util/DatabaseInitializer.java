package com.coaching.util;

import com.coaching.exception.DatabaseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
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
                ensureColumnExists(statement, "students", "password_hash", "TEXT NOT NULL DEFAULT ''");
                ensureColumnExists(statement, "instructors", "password_hash", "TEXT NOT NULL DEFAULT ''");
            }
        } catch (IOException | SQLException e) {
            throw new DatabaseException("Failed to initialize database schema", e);
        }
    }

    private static void ensureColumnExists(Statement statement, String table, String column, String definition) throws SQLException {
        boolean exists = false;
        try (ResultSet rs = statement.executeQuery("PRAGMA table_info(" + table + ")")) {
            while (rs.next()) {
                if (column.equalsIgnoreCase(rs.getString("name"))) {
                    exists = true;
                    break;
                }
            }
        }
        if (!exists) {
            statement.execute("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
        }
    }
}
