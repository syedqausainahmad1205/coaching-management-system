package com.coaching.dao;

import com.coaching.exception.DatabaseException;
import com.coaching.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportingDAO {
    public Map<String, String> summary() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Students", String.valueOf(count("students")));
        map.put("Instructors", String.valueOf(count("instructors")));
        map.put("Courses", String.valueOf(count("courses")));
        map.put("Enrollments", String.valueOf(count("enrollments")));
        map.put("Attendance Records", String.valueOf(count("attendance")));
        map.put("Payments", String.valueOf(count("payments")));
        map.put("Collected Fees", String.format("%.2f", sumPayments()));
        return map;
    }

    private int count(String table) {
        String sql = "SELECT COUNT(*) FROM " + table;
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count " + table, e);
        }
    }

    private double sumPayments() {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("SELECT COALESCE(SUM(amount),0) FROM payments");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to sum payments", e);
        }
    }
}
