package ru.otus.spring.web_ui_book_info_app.repository.author;

import ru.otus.spring.web_ui_book_info_app.domain.Author;

import java.util.Optional;

public interface AuthorRepositoryCustom {
    Optional<Author> update(Author author);

    Optional<String> delete(String id);
}
