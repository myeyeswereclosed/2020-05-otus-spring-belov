package ru.otus.spring.rest_book_info_app.infrastructure;

import org.slf4j.LoggerFactory;

public class AppLoggerFactory {
    public static AppLogger logger(Class<?> clazz) {
        return new AppLogger(LoggerFactory.getLogger(clazz));
    }
}
