package ru.otus.spring.actuator.repository.author;

import ru.otus.spring.actuator.domain.Author;

import java.util.Optional;

public interface AuthorRepositoryCustom {
    Optional<Author> update(Author author);

    Optional<String> delete(String id);
}
