package ru.otus.spring.actuator.repository.genre;

import ru.otus.spring.actuator.domain.Genre;

import java.util.Optional;

public interface GenreRepositoryCustom {
    Optional<Genre> update(Genre genre);

    Optional<String> delete(String id);
}
