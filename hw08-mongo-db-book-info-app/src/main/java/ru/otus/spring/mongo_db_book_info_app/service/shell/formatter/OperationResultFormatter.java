package ru.otus.spring.mongo_db_book_info_app.service.shell.formatter;

public interface OperationResultFormatter<T> {
    String editInfo(T edited);

    String removeInfo(String id);

    String info(T item);
}
