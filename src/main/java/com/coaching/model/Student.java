package com.coaching.model;

public record Student(Integer id, String name, String email, String phone, String address, String enrollmentDate,
                      String passwordHash) {}
