package com.coaching.dao;

import com.coaching.exception.DatabaseException;
import com.coaching.model.Course;
import com.coaching.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public List<Course> findAll(String keyword) {
        String sql = "SELECT id,name,description,duration,fee,instructor_id,capacity,enrolled_students FROM courses WHERE (?='' OR name LIKE ?) ORDER BY id DESC";
        List<Course> result = new ArrayList<>();
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            String key = keyword == null ? "" : keyword.trim();
            ps.setString(1, key);
            ps.setString(2, "%" + key + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Course(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                            rs.getString("duration"), rs.getDouble("fee"), (Integer) rs.getObject("instructor_id"),
                            rs.getInt("capacity"), rs.getInt("enrolled_students")));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch courses", e);
        }
    }

    public void insert(Course course) {
        String sql = "INSERT INTO courses(name,description,duration,fee,instructor_id,capacity,enrolled_students) VALUES(?,?,?,?,?,?,?)";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setString(1, course.name());
            ps.setString(2, course.description());
            ps.setString(3, course.duration());
            ps.setDouble(4, course.fee());
            if (course.instructorId() == null) {
                ps.setObject(5, null);
            } else {
                ps.setInt(5, course.instructorId());
            }
            ps.setInt(6, course.capacity());
            ps.setInt(7, course.enrolledStudents());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert course", e);
        }
    }

    public void update(Course course) {
        String sql = "UPDATE courses SET name=?,description=?,duration=?,fee=?,instructor_id=?,capacity=?,enrolled_students=? WHERE id=?";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setString(1, course.name());
            ps.setString(2, course.description());
            ps.setString(3, course.duration());
            ps.setDouble(4, course.fee());
            if (course.instructorId() == null) {
                ps.setObject(5, null);
            } else {
                ps.setInt(5, course.instructorId());
            }
            ps.setInt(6, course.capacity());
            ps.setInt(7, course.enrolledStudents());
            ps.setInt(8, course.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update course", e);
        }
    }

    public void delete(int id) {
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement("DELETE FROM courses WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete course", e);
        }
    }

    public void adjustEnrollmentCount(int courseId, int delta) {
        String sql = "UPDATE courses SET enrolled_students = enrolled_students + ? WHERE id = ?";
        try (DatabaseConnection.PooledConnection pooled = DatabaseConnection.getConnection();
             PreparedStatement ps = pooled.unwrap().prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to adjust course enrollment count", e);
        }
    }
}
