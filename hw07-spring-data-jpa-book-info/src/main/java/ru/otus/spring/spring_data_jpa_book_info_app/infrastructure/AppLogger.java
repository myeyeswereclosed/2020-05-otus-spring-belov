package ru.otus.spring.spring_data_jpa_book_info_app.infrastructure;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

public class AppLogger {
    private final Logger logger;

    public AppLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void error(String message) {
        logger.error(message);
    }

    public void logException(Throwable e) {
        logger.error("Exception {} occured. Trace:\r\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void warn(String message, Object... objects) {
        logger.warn(message, objects);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String message, Object... objects) {
        logger.info(message, objects);
    }

}
