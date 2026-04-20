package com.coaching.model;

public record Course(Integer id, String name, String description, String duration, double fee, Integer instructorId,
                     int capacity, int enrolledStudents) {}
