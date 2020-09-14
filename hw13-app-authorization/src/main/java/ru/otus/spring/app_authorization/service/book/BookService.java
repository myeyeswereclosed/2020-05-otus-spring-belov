package ru.otus.spring.app_authorization.service.book;

import ru.otus.spring.app_authorization.domain.Book;
import ru.otus.spring.app_authorization.service.result.ServiceResult;

import java.util.List;

public interface BookService {
    ServiceResult<Book> addBook(Book book);

    ServiceResult<Book> rename(Book book);

    ServiceResult<String> remove(String id);

    ServiceResult<List<Book>> getAll();
}
