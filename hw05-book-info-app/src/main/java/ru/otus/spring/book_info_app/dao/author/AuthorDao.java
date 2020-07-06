package ru.otus.spring.book_info_app.dao.author;

import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;

import java.util.List;

public interface AuthorDao {
    Author save(String name);

    void update(long id, String name);

    void delete(long id);

    Author findById(long id);

    List<Author> findByNames(List<String> names);

    Author findByName(String name);

    void save(List<String> names);

    List<Author> findByBook(Book book);
}
