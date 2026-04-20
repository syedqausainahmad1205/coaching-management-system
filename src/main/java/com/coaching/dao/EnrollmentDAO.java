package com.coaching.dao;

import com.coaching.exception.DatabaseException;
import com.coaching.model.Enrollment;
import com.coaching.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    public List<Enrollment> findAll() {
        List<Enrollment> result = new ArrayList<>();
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("SELECT id,student_id,course_id,enrollment_date,status FROM enrollments ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Enrollment(rs.getInt("id"), rs.getInt("student_id"), rs.getInt("course_id"),
                        rs.getString("enrollment_date"), rs.getString("status")));
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch enrollments", e);
        }
    }

    public void insert(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments(student_id,course_id,enrollment_date,status) VALUES(?,?,?,?)";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setInt(1, enrollment.studentId());
            ps.setInt(2, enrollment.courseId());
            ps.setString(3, enrollment.enrollmentDate());
            ps.setString(4, enrollment.status());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert enrollment", e);
        }
    }

    public void updateStatus(int id, String status) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("UPDATE enrollments SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update enrollment status", e);
        }
    }

    public void delete(int id) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("DELETE FROM enrollments WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete enrollment", e);
        }
    }
}
