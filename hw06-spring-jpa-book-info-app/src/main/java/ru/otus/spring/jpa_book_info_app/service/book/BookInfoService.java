package ru.otus.spring.jpa_book_info_app.service.book;

import ru.otus.spring.jpa_book_info_app.domain.*;
import ru.otus.spring.jpa_book_info_app.dto.BookInfo;
import ru.otus.spring.jpa_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface BookInfoService {
    ServiceResult<Book> addBookAuthor(long bookId, Author author);

    ServiceResult<Book> addBookGenre(long bookId, Genre genre);

    ServiceResult<Void> addComment(long bookId, Comment comment);

    ServiceResult<BookInfo> get(long bookId);

    ServiceResult<List<BookInfo>> getAll();
}
