package com.coaching.model;

public record Payment(Integer id, int studentId, int courseId, double amount, String paymentDate, String status) {}
