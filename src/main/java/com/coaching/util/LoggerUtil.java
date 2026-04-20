package com.coaching.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoggerUtil {
    private LoggerUtil() {
    }

    public static Logger getLogger(Class<?> cls) {
        Logger logger = Logger.getLogger(cls.getName());
        logger.setLevel(Level.INFO);
        return logger;
    }
}
