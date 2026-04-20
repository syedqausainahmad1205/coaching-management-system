package com.coaching.service;

import com.coaching.dao.PaymentDAO;
import com.coaching.model.Payment;
import com.coaching.util.DateUtil;
import com.coaching.util.ValidationUtil;

import java.util.List;

public class PaymentService {
    private final PaymentDAO dao = new PaymentDAO();

    public List<Payment> findAll() {
        return dao.findAll();
    }

    public void create(String studentId, String courseId, String amount, String status) {
        int sid = ValidationUtil.parsePositiveInt(studentId, "Student ID");
        int cid = ValidationUtil.parsePositiveInt(courseId, "Course ID");
        double parsedAmount = ValidationUtil.parsePositiveDouble(amount, "Amount");
        ValidationUtil.requireNonBlank(status, "Status");
        dao.insert(new Payment(null, sid, cid, parsedAmount, DateUtil.today(), status.trim()));
    }

    public void updateStatus(int id, String status) {
        ValidationUtil.requireNonBlank(status, "Status");
        dao.updateStatus(id, status.trim());
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
