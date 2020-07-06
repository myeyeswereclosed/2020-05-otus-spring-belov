package ru.otus.spring.book_info_app.dao.genre;

import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import java.util.List;

public interface GenreDao {
    Genre save(String name);

    void update(long id, String name);

    void delete(long id);

    Genre findByName(String name);

    List<Genre> findByNames(List<String> names);

    List<Genre> findByBook(Book book);
}
