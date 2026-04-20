package com.coaching.service;

import com.coaching.dao.StudentDAO;
import com.coaching.exception.ValidationException;
import com.coaching.model.Student;
import com.coaching.util.PasswordUtil;
import com.coaching.util.ValidationUtil;

import java.util.List;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();

    public List<Student> findAll(String keyword) {
        return dao.findAll(keyword);
    }

    public void create(String name, String email, String phone, String address, String enrollmentDate, String password) {
        ValidationUtil.requireNonBlank(name, "Name");
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePassword(password);
        dao.insert(new Student(null, name.trim(), email.trim(), phone == null ? "" : phone.trim(),
                address == null ? "" : address.trim(), enrollmentDate, PasswordUtil.hash(password)));
    }

    public void update(int id, String name, String email, String phone, String address, String enrollmentDate, String password) {
        ValidationUtil.requireNonBlank(name, "Name");
        ValidationUtil.validateEmail(email);
        Student existing = dao.findById(id);
        if (existing == null) {
            throw new ValidationException("Student not found");
        }
        String passwordHash = password == null || password.isBlank()
                ? existing.passwordHash()
                : PasswordUtil.hash(password);
        dao.update(new Student(id, name.trim(), email.trim(), phone == null ? "" : phone.trim(),
                address == null ? "" : address.trim(), enrollmentDate, passwordHash));
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
