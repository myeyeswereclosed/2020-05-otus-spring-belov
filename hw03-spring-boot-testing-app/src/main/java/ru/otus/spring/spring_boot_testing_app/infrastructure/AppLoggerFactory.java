package ru.otus.spring.spring_boot_testing_app.infrastructure;

import org.slf4j.LoggerFactory;

public class AppLoggerFactory {
    public static AppLogger logger(Class<?> clazz) {
        return new AppLogger(LoggerFactory.getLogger(clazz));
    }
}
