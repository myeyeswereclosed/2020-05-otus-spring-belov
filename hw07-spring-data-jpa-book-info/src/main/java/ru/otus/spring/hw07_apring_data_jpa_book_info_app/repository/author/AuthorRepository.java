package ru.otus.spring.hw07_apring_data_jpa_book_info_app.repository.author;

import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.dto.BookAuthor;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author save(Author author);

    boolean delete(long id);

    List<Author> findAll();

    List<BookAuthor> findAllWithBooks();

    Optional<Author> findByFirstAndLastName(String firstName, String lastName);
}
