package ru.otus.spring.app_authentication.repository.genre;

import ru.otus.spring.app_authentication.domain.Genre;

import java.util.Optional;

public interface GenreRepositoryCustom {
    Optional<Genre> update(Genre genre);

    Optional<String> delete(String id);
}
