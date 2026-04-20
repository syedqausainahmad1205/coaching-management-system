package com.coaching.util;

import com.coaching.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationUtilTest {
    @Test
    void parsesPositiveInt() {
        assertEquals(10, ValidationUtil.parsePositiveInt("10", "Capacity"));
    }

    @Test
    void rejectsInvalidEmail() {
        assertThrows(ValidationException.class, () -> ValidationUtil.validateEmail("bad-email"));
    }
}
