package ru.otus.spring.mongo_db_book_info_app.repository.author;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
