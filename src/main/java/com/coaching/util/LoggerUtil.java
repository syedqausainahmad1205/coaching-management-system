package com.coaching.util;

import java.util.logging.Logger;

public final class LoggerUtil {
    private LoggerUtil() {
    }

    public static Logger getLogger(Class<?> cls) {
        return Logger.getLogger(cls.getName());
    }
}
