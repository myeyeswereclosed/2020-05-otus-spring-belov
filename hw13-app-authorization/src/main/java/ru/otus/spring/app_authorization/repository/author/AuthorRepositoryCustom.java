package ru.otus.spring.app_authorization.repository.author;

import ru.otus.spring.app_authorization.domain.Author;

import java.util.Optional;

public interface AuthorRepositoryCustom {
    Optional<Author> update(Author author);

    Optional<String> delete(String id);
}
