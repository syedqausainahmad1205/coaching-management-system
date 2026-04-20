package com.coaching.dao;

import com.coaching.exception.DatabaseException;
import com.coaching.model.Payment;
import com.coaching.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    public List<Payment> findAll() {
        List<Payment> result = new ArrayList<>();
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("SELECT id,student_id,course_id,amount,payment_date,status FROM payments ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Payment(rs.getInt("id"), rs.getInt("student_id"), rs.getInt("course_id"),
                        rs.getDouble("amount"), rs.getString("payment_date"), rs.getString("status")));
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch payments", e);
        }
    }

    public void insert(Payment payment) {
        String sql = "INSERT INTO payments(student_id,course_id,amount,payment_date,status) VALUES(?,?,?,?,?)";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setInt(1, payment.studentId());
            ps.setInt(2, payment.courseId());
            ps.setDouble(3, payment.amount());
            ps.setString(4, payment.paymentDate());
            ps.setString(5, payment.status());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert payment", e);
        }
    }

    public void updateStatus(int id, String status) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("UPDATE payments SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update payment status", e);
        }
    }

    public void delete(int id) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("DELETE FROM payments WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete payment", e);
        }
    }
}
