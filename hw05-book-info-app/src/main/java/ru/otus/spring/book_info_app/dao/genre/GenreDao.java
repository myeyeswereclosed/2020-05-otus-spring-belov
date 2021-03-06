package ru.otus.spring.book_info_app.dao.genre;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import java.util.List;

public interface GenreDao {
    Genre save(Genre genre);

    void update(Genre genre);

    void delete(long id);

    Genre findByName(String name);

    List<Genre> findByBook(Book book);

    List<Pair<Genre, Long>> findAllWithBooks();
}
