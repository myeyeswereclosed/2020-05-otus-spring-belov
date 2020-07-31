package ru.otus.spring.mongo_db_book_info_app.repository.genre;

import ru.otus.spring.mongo_db_book_info_app.domain.Genre;

import java.util.Optional;

public interface GenreRepositoryCustom {
    Optional<Genre> update(Genre genre);

    Optional<String> delete(String id);
}
