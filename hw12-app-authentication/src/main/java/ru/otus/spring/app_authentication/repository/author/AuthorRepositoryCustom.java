package ru.otus.spring.app_authentication.repository.author;

import org.springframework.stereotype.Repository;
import ru.otus.spring.app_authentication.domain.Author;

import java.util.Optional;

public interface AuthorRepositoryCustom {
    Optional<Author> update(Author author);

    Optional<String> delete(String id);
}
