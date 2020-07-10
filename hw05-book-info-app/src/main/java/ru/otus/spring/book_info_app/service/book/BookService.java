package ru.otus.spring.book_info_app.service.book;

import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

public interface BookService {
    ServiceResult<Book> addBook(Book book);

    ServiceResult<Void> rename(Book book);

    ServiceResult<Void> remove(long id);
}
