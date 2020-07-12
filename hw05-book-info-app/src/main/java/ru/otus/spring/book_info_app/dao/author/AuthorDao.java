package ru.otus.spring.book_info_app.dao.author;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;

import java.util.List;

public interface AuthorDao {
    Author save(Author author);

    void update(Author author);

    void delete(long id);

    Author findByFirstAndLastName(String firstName, String lastName);

    List<Author> findByBook(Book book);

    List<Pair<Author, Long>> findAllWithBooks();
}
