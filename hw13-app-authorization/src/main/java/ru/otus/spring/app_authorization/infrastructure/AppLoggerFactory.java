package ru.otus.spring.app_authorization.infrastructure;

import org.slf4j.LoggerFactory;

public class AppLoggerFactory {
    public static AppLogger logger(Class<?> clazz) {
        return new AppLogger(LoggerFactory.getLogger(clazz));
    }
}
