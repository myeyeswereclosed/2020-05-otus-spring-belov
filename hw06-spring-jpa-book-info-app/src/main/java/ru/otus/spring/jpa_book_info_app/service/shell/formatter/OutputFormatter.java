package ru.otus.spring.jpa_book_info_app.service.shell.formatter;

public interface OutputFormatter<T> {
    String format(T entity);
}
