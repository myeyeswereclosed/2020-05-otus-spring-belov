package ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.book;

import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface BookInfoService {
    ServiceResult<Book> addBookAuthor(long bookId, Author author);

    ServiceResult<Book> addBookGenre(long bookId, Genre genre);

    ServiceResult<Book> addComment(long bookId, Comment comment);

    ServiceResult<Book> get(long bookId);

    ServiceResult<List<Book>> getAll();
}
