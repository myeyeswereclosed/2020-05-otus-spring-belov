package ru.otus.spring.jpa_book_info_app.repository.author;

import ru.otus.spring.jpa_book_info_app.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author save(Author book);

    Optional<Author> findById(long id);

    void delete(long id);

    List<Author> findAll();
}
