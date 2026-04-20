package com.coaching.service;

import com.coaching.dao.EnrollmentDAO;
import com.coaching.model.Enrollment;
import com.coaching.util.DateUtil;
import com.coaching.util.ValidationUtil;

import java.util.List;

public class EnrollmentService {
    private final EnrollmentDAO dao = new EnrollmentDAO();
    private final CourseService courseService = new CourseService();

    public List<Enrollment> findAll() {
        return dao.findAll();
    }

    public void create(String studentId, String courseId, String status) {
        int sid = ValidationUtil.parseNonNegativeInt(studentId, "Student ID");
        int cid = ValidationUtil.parseNonNegativeInt(courseId, "Course ID");
        ValidationUtil.requireNonBlank(status, "Status");
        dao.insert(new Enrollment(null, sid, cid, DateUtil.today(), status.trim()));
        courseService.incrementEnrollment(cid);
    }

    public void updateStatus(int id, String status) {
        ValidationUtil.requireNonBlank(status, "Status");
        dao.updateStatus(id, status.trim());
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
