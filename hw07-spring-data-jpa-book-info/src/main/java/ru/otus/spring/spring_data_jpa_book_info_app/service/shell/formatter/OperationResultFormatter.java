package ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter;

public interface OperationResultFormatter<T> {
    String editInfo(T edited);

    String removeInfo(long id);

    String info(T item);
}
