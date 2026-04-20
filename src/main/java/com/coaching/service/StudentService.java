package com.coaching.service;

import com.coaching.dao.StudentDAO;
import com.coaching.model.Student;
import com.coaching.util.ValidationUtil;

import java.util.List;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();

    public List<Student> findAll(String keyword) {
        return dao.findAll(keyword);
    }

    public void create(String name, String email, String phone, String address, String enrollmentDate) {
        ValidationUtil.requireNonBlank(name, "Name");
        ValidationUtil.validateEmail(email);
        dao.insert(new Student(null, name.trim(), email.trim(), phone == null ? "" : phone.trim(),
                address == null ? "" : address.trim(), enrollmentDate));
    }

    public void update(int id, String name, String email, String phone, String address, String enrollmentDate) {
        ValidationUtil.requireNonBlank(name, "Name");
        ValidationUtil.validateEmail(email);
        dao.update(new Student(id, name.trim(), email.trim(), phone == null ? "" : phone.trim(),
                address == null ? "" : address.trim(), enrollmentDate));
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
