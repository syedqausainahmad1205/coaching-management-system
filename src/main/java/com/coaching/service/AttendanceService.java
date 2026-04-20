package com.coaching.service;

import com.coaching.dao.AttendanceDAO;
import com.coaching.model.Attendance;
import com.coaching.util.DateUtil;
import com.coaching.util.ValidationUtil;

import java.util.List;

public class AttendanceService {
    private final AttendanceDAO dao = new AttendanceDAO();

    public List<Attendance> findAll() {
        return dao.findAll();
    }

    public void create(String studentId, String courseId, String status) {
        int sid = ValidationUtil.parsePositiveInt(studentId, "Student ID");
        int cid = ValidationUtil.parsePositiveInt(courseId, "Course ID");
        ValidationUtil.requireNonBlank(status, "Status");
        dao.insert(new Attendance(null, sid, cid, DateUtil.today(), status.trim()));
    }

    public void updateStatus(int id, String status) {
        ValidationUtil.requireNonBlank(status, "Status");
        dao.updateStatus(id, status.trim());
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
