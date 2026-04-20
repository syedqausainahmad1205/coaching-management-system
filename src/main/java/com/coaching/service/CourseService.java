package com.coaching.service;

import com.coaching.dao.CourseDAO;
import com.coaching.model.Course;
import com.coaching.util.ValidationUtil;

import java.util.List;

public class CourseService {
    private final CourseDAO dao = new CourseDAO();

    public List<Course> findAll(String keyword) {
        return dao.findAll(keyword);
    }

    public void create(String name, String description, String duration, String fee, String instructorId,
                       String capacity, String enrolledStudents) {
        ValidationUtil.requireNonBlank(name, "Name");
        double parsedFee = ValidationUtil.parseNonNegativeDouble(fee, "Fee");
        int parsedCapacity = ValidationUtil.parseNonNegativeInt(capacity, "Capacity");
        int parsedEnrolled = ValidationUtil.parseNonNegativeInt(enrolledStudents, "Enrolled Students");
        Integer parsedInstructor = parseOptionalInt(instructorId);
        dao.insert(new Course(null, name.trim(), trim(description), trim(duration), parsedFee, parsedInstructor,
                parsedCapacity, parsedEnrolled));
    }

    public void update(int id, String name, String description, String duration, String fee, String instructorId,
                       String capacity, String enrolledStudents) {
        ValidationUtil.requireNonBlank(name, "Name");
        double parsedFee = ValidationUtil.parseNonNegativeDouble(fee, "Fee");
        int parsedCapacity = ValidationUtil.parseNonNegativeInt(capacity, "Capacity");
        int parsedEnrolled = ValidationUtil.parseNonNegativeInt(enrolledStudents, "Enrolled Students");
        Integer parsedInstructor = parseOptionalInt(instructorId);
        dao.update(new Course(id, name.trim(), trim(description), trim(duration), parsedFee, parsedInstructor,
                parsedCapacity, parsedEnrolled));
    }

    public void delete(int id) {
        dao.delete(id);
    }

    public void incrementEnrollment(int courseId) {
        dao.adjustEnrollmentCount(courseId, 1);
    }

    private Integer parseOptionalInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return ValidationUtil.parseNonNegativeInt(value, "Instructor ID");
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
