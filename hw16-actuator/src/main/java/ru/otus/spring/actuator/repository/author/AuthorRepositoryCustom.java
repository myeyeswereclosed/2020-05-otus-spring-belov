package ru.otus.spring.actuator.repository.author;

import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.repository.DuplicatesFinder;

import java.util.Optional;

public interface AuthorRepositoryCustom extends DuplicatesFinder<Author> {
    Optional<Author> update(Author author);

    Optional<String> delete(String id);
}
