package ru.otus.spring.hw18.book_service.repository.genre;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.hw18.book_service.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String>, GenreRepositoryCustom {
    List<Genre> findAll();

    Optional<Genre> findByName(String name);

}
