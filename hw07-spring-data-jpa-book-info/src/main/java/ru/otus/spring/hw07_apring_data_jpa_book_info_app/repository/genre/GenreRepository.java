package ru.otus.spring.hw07_apring_data_jpa_book_info_app.repository.genre;

import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.dto.BookGenre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Genre save(Genre genre);

    boolean delete(int id);

    List<Genre> findAll();

    List<BookGenre> findAllWithBooks();

    Optional<Genre> findByName(String name);
}
