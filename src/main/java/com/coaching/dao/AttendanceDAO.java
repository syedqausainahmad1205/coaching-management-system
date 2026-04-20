package com.coaching.dao;

import com.coaching.exception.DatabaseException;
import com.coaching.model.Attendance;
import com.coaching.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    public List<Attendance> findAll() {
        List<Attendance> result = new ArrayList<>();
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("SELECT id,student_id,course_id,date,status FROM attendance ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Attendance(rs.getInt("id"), rs.getInt("student_id"), rs.getInt("course_id"),
                        rs.getString("date"), rs.getString("status")));
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch attendance", e);
        }
    }

    public void insert(Attendance attendance) {
        String sql = "INSERT INTO attendance(student_id,course_id,date,status) VALUES(?,?,?,?)";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setInt(1, attendance.studentId());
            ps.setInt(2, attendance.courseId());
            ps.setString(3, attendance.date());
            ps.setString(4, attendance.status());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert attendance", e);
        }
    }

    public void updateStatus(int id, String status) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("UPDATE attendance SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update attendance status", e);
        }
    }

    public void delete(int id) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("DELETE FROM attendance WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete attendance", e);
        }
    }
}
