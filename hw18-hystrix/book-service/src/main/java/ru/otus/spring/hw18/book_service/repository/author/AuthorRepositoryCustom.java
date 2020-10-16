package ru.otus.spring.hw18.book_service.repository.author;

import ru.otus.spring.hw18.book_service.domain.Author;
import ru.otus.spring.hw18.book_service.repository.DuplicatesFinder;

import java.util.Optional;

public interface AuthorRepositoryCustom extends DuplicatesFinder<Author> {
    Optional<Author> update(Author author);

    Optional<String> delete(String id);
}
