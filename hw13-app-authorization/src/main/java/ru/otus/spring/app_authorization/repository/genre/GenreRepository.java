package ru.otus.spring.app_authorization.repository.genre;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.app_authorization.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String>, GenreRepositoryCustom {
    Optional<Genre> findByName(String name);
}
