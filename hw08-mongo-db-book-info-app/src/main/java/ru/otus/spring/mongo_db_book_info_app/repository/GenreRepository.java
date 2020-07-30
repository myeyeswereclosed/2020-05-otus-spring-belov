package ru.otus.spring.mongo_db_book_info_app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {
    Optional<Genre> findByName(String name);
}
