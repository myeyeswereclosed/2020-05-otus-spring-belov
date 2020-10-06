package ru.otus.spring.actuator.repository.genre;

import ru.otus.spring.actuator.domain.Genre;
import ru.otus.spring.actuator.repository.FindDuplicates;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryCustom extends FindDuplicates<Genre> {
    Optional<Genre> update(Genre genre);

    Optional<String> delete(String id);

    List<Genre> findDuplicates();
}
