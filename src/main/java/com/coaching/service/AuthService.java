package com.coaching.service;

import com.coaching.dao.InstructorDAO;
import com.coaching.dao.StudentDAO;
import com.coaching.model.Instructor;
import com.coaching.model.Student;
import com.coaching.util.PasswordUtil;
import com.coaching.util.ValidationUtil;

public class AuthService {
    private final StudentDAO studentDAO = new StudentDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();

    public boolean authenticateStudent(String email, String password) {
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePassword(password);
        Student student = studentDAO.findByEmail(email.trim());
        return student != null && PasswordUtil.verify(password, student.passwordHash());
    }

    public boolean authenticateTeacher(String email, String password) {
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePassword(password);
        Instructor teacher = instructorDAO.findByEmail(email.trim());
        return teacher != null && PasswordUtil.verify(password, teacher.passwordHash());
    }
}
