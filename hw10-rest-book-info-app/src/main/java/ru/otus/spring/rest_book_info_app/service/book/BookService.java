package ru.otus.spring.rest_book_info_app.service.book;

import ru.otus.spring.rest_book_info_app.domain.Book;
import ru.otus.spring.rest_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface BookService {
    ServiceResult<Book> addBook(Book book);

    ServiceResult<Book> rename(Book book);

    ServiceResult<String> remove(String id);

    ServiceResult<List<Book>> getAll();
}
