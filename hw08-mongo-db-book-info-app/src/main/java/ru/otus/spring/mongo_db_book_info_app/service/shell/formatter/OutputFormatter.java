package ru.otus.spring.mongo_db_book_info_app.service.shell.formatter;

public interface OutputFormatter<T> {
    String format(T entity);
}
