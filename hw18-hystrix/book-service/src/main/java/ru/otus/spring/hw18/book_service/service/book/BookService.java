package ru.otus.spring.hw18.book_service.service.book;

import ru.otus.spring.hw18.book_service.domain.Book;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

import java.util.List;

public interface BookService {
    ServiceResult<Book> addBook(Book book);

    ServiceResult<Book> rename(Book book);

    ServiceResult<String> remove(String id);

    ServiceResult<List<Book>> getAll();
}
