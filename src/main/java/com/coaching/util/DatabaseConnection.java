package com.coaching.util;

import com.coaching.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class DatabaseConnection {
    private static final String URL = ConfigManager.get("db.url", "jdbc:sqlite:coaching.db");
    private static final int POOL_SIZE = Integer.parseInt(ConfigManager.get("db.poolSize", "5"));
    private static final BlockingQueue<Connection> POOL = new ArrayBlockingQueue<>(POOL_SIZE);

    static {
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                POOL.add(DriverManager.getConnection(URL));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Unable to initialize DB pool", e);
        }
    }

    private DatabaseConnection() {
    }

    public static PooledConnection getConnection() {
        try {
            return new PooledConnection(POOL.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseException("Interrupted while getting DB connection", e);
        }
    }

    static void release(Connection connection) {
        if (connection != null) {
            POOL.offer(connection);
        }
    }

    public static final class PooledConnection implements AutoCloseable {
        private Connection connection;

        private PooledConnection(Connection connection) {
            this.connection = connection;
        }

        public Connection unwrap() {
            return connection;
        }

        @Override
        public void close() {
            if (connection != null) {
                DatabaseConnection.release(connection);
                connection = null;
            }
        }
    }
}
