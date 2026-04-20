package com.coaching.service;

import com.coaching.dao.InstructorDAO;
import com.coaching.model.Instructor;
import com.coaching.util.ValidationUtil;

import java.util.List;

public class InstructorService {
    private final InstructorDAO dao = new InstructorDAO();

    public List<Instructor> findAll(String keyword) {
        return dao.findAll(keyword);
    }

    public void create(String name, String email, String phone, String specialization, String availability) {
        ValidationUtil.requireNonBlank(name, "Name");
        ValidationUtil.validateEmail(email);
        dao.insert(new Instructor(null, name.trim(), email.trim(), phone == null ? "" : phone.trim(),
                specialization == null ? "" : specialization.trim(), availability == null ? "" : availability.trim()));
    }

    public void update(int id, String name, String email, String phone, String specialization, String availability) {
        ValidationUtil.requireNonBlank(name, "Name");
        ValidationUtil.validateEmail(email);
        dao.update(new Instructor(id, name.trim(), email.trim(), phone == null ? "" : phone.trim(),
                specialization == null ? "" : specialization.trim(), availability == null ? "" : availability.trim()));
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
