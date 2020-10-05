package ru.otus.spring.actuator.repository.author;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.actuator.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
