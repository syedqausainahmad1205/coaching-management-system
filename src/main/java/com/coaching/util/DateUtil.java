package com.coaching.util;

import java.time.LocalDate;

public final class DateUtil {
    private DateUtil() {
    }

    public static String today() {
        return LocalDate.now().toString();
    }
}
