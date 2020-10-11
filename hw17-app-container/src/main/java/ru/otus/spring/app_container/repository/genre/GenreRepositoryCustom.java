package ru.otus.spring.app_container.repository.genre;

import ru.otus.spring.app_container.domain.Genre;
import ru.otus.spring.app_container.repository.DuplicatesFinder;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryCustom extends DuplicatesFinder<Genre> {
    Optional<Genre> update(Genre genre);

    Optional<String> delete(String id);

    List<Genre> findDuplicates();
}
