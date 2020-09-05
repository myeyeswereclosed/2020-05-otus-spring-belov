package ru.otus.spring.reactive_book_info_app.repository.genre;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Mono<Genre> findByName(String name);
}
