package ru.otus.spring.hw18.book_service.repository.author;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.hw18.book_service.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {
    List<Author> findAll();

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
