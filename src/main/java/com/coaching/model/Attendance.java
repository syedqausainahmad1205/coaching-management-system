package com.coaching.model;

public record Attendance(Integer id, int studentId, int courseId, String date, String status) {}
