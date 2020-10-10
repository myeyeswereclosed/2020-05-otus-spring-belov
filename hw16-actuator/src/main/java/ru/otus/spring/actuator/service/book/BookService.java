package ru.otus.spring.actuator.service.book;

import ru.otus.spring.actuator.domain.Book;
import ru.otus.spring.actuator.service.result.ServiceResult;

import java.util.List;

public interface BookService {
    ServiceResult<Book> addBook(Book book);

    ServiceResult<Book> rename(Book book);

    ServiceResult<String> remove(String id);

    ServiceResult<List<Book>> getAll();
}
