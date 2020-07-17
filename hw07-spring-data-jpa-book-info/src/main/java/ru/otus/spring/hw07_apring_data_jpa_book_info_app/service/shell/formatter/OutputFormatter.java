package ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.shell.formatter;

public interface OutputFormatter<T> {
    String format(T entity);
}
