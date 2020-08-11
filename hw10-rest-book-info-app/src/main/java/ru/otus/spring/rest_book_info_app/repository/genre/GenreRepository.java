package ru.otus.spring.rest_book_info_app.repository.genre;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.rest_book_info_app.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String>, GenreRepositoryCustom {
    Optional<Genre> findByName(String name);
}
