package ru.otus.spring.hw18.book_service.repository.genre;

import ru.otus.spring.hw18.book_service.domain.Genre;
import ru.otus.spring.hw18.book_service.repository.DuplicatesFinder;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryCustom extends DuplicatesFinder<Genre> {
    Optional<Genre> update(Genre genre);

    Optional<String> delete(String id);

    List<Genre> findDuplicates();
}
