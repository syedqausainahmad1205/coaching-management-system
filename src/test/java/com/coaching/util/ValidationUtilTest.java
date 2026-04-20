package com.coaching.util;

import com.coaching.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationUtilTest {
    @Test
    void parsesNonNegativeInt() {
        assertEquals(10, ValidationUtil.parseNonNegativeInt("10", "Capacity"));
        assertEquals(0, ValidationUtil.parseNonNegativeInt("0", "Capacity"));
    }

    @Test
    void parsesNonNegativeDouble() {
        assertEquals(0.0, ValidationUtil.parseNonNegativeDouble("0", "Amount"));
        assertEquals(12.5, ValidationUtil.parseNonNegativeDouble("12.5", "Amount"));
    }

    @Test
    void validatesRequiredField() {
        assertThrows(ValidationException.class, () -> ValidationUtil.requireNonBlank(" ", "Name"));
    }

    @Test
    void acceptsValidEmail() {
        ValidationUtil.validateEmail("student@example.com");
    }

    @Test
    void rejectsInvalidEmail() {
        assertThrows(ValidationException.class, () -> ValidationUtil.validateEmail("bad-email"));
        assertThrows(ValidationException.class, () -> ValidationUtil.validateEmail("user@domain"));
        assertThrows(ValidationException.class, () -> ValidationUtil.validateEmail("user@domain..com"));
        assertThrows(ValidationException.class, () -> ValidationUtil.validateEmail(".user@domain.com"));
    }

    @Test
    void validatesPassword() {
        ValidationUtil.validatePassword("secret1");
        assertThrows(ValidationException.class, () -> ValidationUtil.validatePassword("123"));
    }
}
