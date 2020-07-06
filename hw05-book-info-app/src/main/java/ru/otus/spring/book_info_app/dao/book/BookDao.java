package ru.otus.spring.book_info_app.dao.book;

import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import java.util.List;

public interface BookDao {
    Book save(String title);

    Book findById(long id);

    void update(long id, String title);

    void delete(long id);

    List<Book> findAll();

    void addAuthor(long id, Author author);

    void addGenre(long id, Genre genre);
}
