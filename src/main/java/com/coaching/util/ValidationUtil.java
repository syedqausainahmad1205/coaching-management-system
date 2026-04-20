package com.coaching.util;

import com.coaching.exception.ValidationException;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static void requireNonBlank(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(field + " is required");
        }
    }

    public static void validateEmail(String email) {
        requireNonBlank(email, "Email");
        if (!email.matches("^[A-Za-z0-9](?:[A-Za-z0-9+_-]|\\.(?=[A-Za-z0-9+_-]))*@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")) {
            throw new ValidationException("Invalid email format");
        }
    }

    public static void validatePassword(String password) {
        requireNonBlank(password, "Password");
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }
    }

    public static int parseNonNegativeInt(String value, String field) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0) {
                throw new ValidationException(field + " must be >= 0");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new ValidationException(field + " must be a valid integer");
        }
    }

    public static double parseNonNegativeDouble(String value, String field) {
        try {
            double parsed = Double.parseDouble(value.trim());
            if (parsed < 0) {
                throw new ValidationException(field + " must be >= 0");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new ValidationException(field + " must be a valid number");
        }
    }
}
