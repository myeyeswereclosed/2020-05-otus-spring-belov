package ru.otus.spring.app_container.repository.author;

import ru.otus.spring.app_container.domain.Author;
import ru.otus.spring.app_container.repository.DuplicatesFinder;

import java.util.Optional;

public interface AuthorRepositoryCustom extends DuplicatesFinder<Author> {
    Optional<Author> update(Author author);

    Optional<String> delete(String id);
}
