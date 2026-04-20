package com.coaching.util;

import com.coaching.exception.DatabaseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public final class DatabaseInitializer {
    private static final Pattern SQL_IDENTIFIER = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

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
        String safeTable = requireSqlIdentifier(table, "table");
        String safeColumn = requireSqlIdentifier(column, "column");
        if (definition == null || !definition.matches("[A-Za-z0-9_ '(),]+")) {
            throw new IllegalArgumentException("Invalid SQL definition");
        }
        boolean exists = false;
        try (ResultSet rs = statement.executeQuery("PRAGMA table_info(" + safeTable + ")")) {
            while (rs.next()) {
                if (safeColumn.equalsIgnoreCase(rs.getString("name"))) {
                    exists = true;
                    break;
                }
            }
        }
        if (!exists) {
            statement.execute("ALTER TABLE " + safeTable + " ADD COLUMN " + safeColumn + " " + definition);
        }
    }

    private static String requireSqlIdentifier(String value, String field) {
        if (value == null || !SQL_IDENTIFIER.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid SQL " + field);
        }
        return value;
    }
}
