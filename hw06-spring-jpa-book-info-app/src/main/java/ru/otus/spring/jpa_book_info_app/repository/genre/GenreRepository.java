package ru.otus.spring.jpa_book_info_app.repository.genre;

import ru.otus.spring.jpa_book_info_app.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Genre save(Genre genre);

    Optional<Genre> findById(long id);

    void delete(long id);

    List<Genre> findAll();
}
