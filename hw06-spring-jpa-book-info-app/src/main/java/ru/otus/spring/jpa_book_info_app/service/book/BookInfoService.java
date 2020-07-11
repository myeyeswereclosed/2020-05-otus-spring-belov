package ru.otus.spring.jpa_book_info_app.service.book;

import ru.otus.spring.jpa_book_info_app.domain.Author;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.dto.BookDto;
import ru.otus.spring.jpa_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface BookInfoService {
//    ServiceResult<Void> addBookAuthor(long bookId, Author author);
//
//    ServiceResult<Void> addBookGenre(long bookId, Genre genre);

    ServiceResult<Book> get(long bookId);

    ServiceResult<List<Book>> getAll();
}
