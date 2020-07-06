package ru.otus.spring.book_info_app.service.book;

import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.List;

public interface BookService {
    ServiceResult<Book> addBook(String title);

    ServiceResult<Book> find(long id);

    ServiceResult<Book> rename(long id, String title);

    ServiceResult<Void> remove(long id);

    ServiceResult<List<Book>> getAll();

    ServiceResult<Void> addAuthor(long bookId, Author author);

    ServiceResult<Void> addGenre(long bookId, Genre genre);
}
