package ru.otus.spring.reactive_book_info_app.repository.author;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    Mono<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
