package com.coaching.dao;

import com.coaching.exception.DatabaseException;
import com.coaching.model.Student;
import com.coaching.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<Student> findAll(String keyword) {
        String sql = "SELECT id,name,email,phone,address,enrollment_date,password_hash FROM students WHERE (?='' OR name LIKE ? OR email LIKE ?) ORDER BY id DESC";
        List<Student> students = new ArrayList<>();
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            String key = keyword == null ? "" : keyword.trim();
            ps.setString(1, key);
            ps.setString(2, "%" + key + "%");
            ps.setString(3, "%" + key + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }
            return students;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch students", e);
        }
    }

    public Student findById(int id) {
        String sql = "SELECT id,name,email,phone,address,enrollment_date,password_hash FROM students WHERE id=?";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch student by id", e);
        }
    }

    public Student findByEmail(String email) {
        String sql = "SELECT id,name,email,phone,address,enrollment_date,password_hash FROM students WHERE email=?";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch student by email", e);
        }
    }

    public void insert(Student student) {
        String sql = "INSERT INTO students(name,email,phone,address,enrollment_date,password_hash) VALUES(?,?,?,?,?,?)";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setString(1, student.name());
            ps.setString(2, student.email());
            ps.setString(3, student.phone());
            ps.setString(4, student.address());
            ps.setString(5, student.enrollmentDate());
            ps.setString(6, student.passwordHash());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert student", e);
        }
    }

    public void update(Student student) {
        String sql = "UPDATE students SET name=?, email=?, phone=?, address=?, enrollment_date=?, password_hash=? WHERE id=?";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setString(1, student.name());
            ps.setString(2, student.email());
            ps.setString(3, student.phone());
            ps.setString(4, student.address());
            ps.setString(5, student.enrollmentDate());
            ps.setString(6, student.passwordHash());
            ps.setInt(7, student.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update student", e);
        }
    }

    public void delete(int id) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("DELETE FROM students WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete student", e);
        }
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                rs.getString("phone"), rs.getString("address"), rs.getString("enrollment_date"),
                rs.getString("password_hash"));
    }
}
