package com.coaching.dao;

import com.coaching.exception.DatabaseException;
import com.coaching.model.Instructor;
import com.coaching.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstructorDAO {
    public List<Instructor> findAll(String keyword) {
        String sql = "SELECT id,name,email,phone,specialization,availability FROM instructors WHERE (?='' OR name LIKE ? OR email LIKE ?) ORDER BY id DESC";
        List<Instructor> result = new ArrayList<>();
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            String key = keyword == null ? "" : keyword.trim();
            ps.setString(1, key);
            ps.setString(2, "%" + key + "%");
            ps.setString(3, "%" + key + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Instructor(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("specialization"), rs.getString("availability")));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch instructors", e);
        }
    }

    public void insert(Instructor instructor) {
        String sql = "INSERT INTO instructors(name,email,phone,specialization,availability) VALUES(?,?,?,?,?)";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setString(1, instructor.name());
            ps.setString(2, instructor.email());
            ps.setString(3, instructor.phone());
            ps.setString(4, instructor.specialization());
            ps.setString(5, instructor.availability());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert instructor", e);
        }
    }

    public void update(Instructor instructor) {
        String sql = "UPDATE instructors SET name=?, email=?, phone=?, specialization=?, availability=? WHERE id=?";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setString(1, instructor.name());
            ps.setString(2, instructor.email());
            ps.setString(3, instructor.phone());
            ps.setString(4, instructor.specialization());
            ps.setString(5, instructor.availability());
            ps.setInt(6, instructor.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update instructor", e);
        }
    }

    public void delete(int id) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("DELETE FROM instructors WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete instructor", e);
        }
    }
}
