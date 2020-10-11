package ru.otus.spring.app_container.repository.author;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.app_container.domain.Author;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {
    @RestResource(path = "authors", rel = "authors")
    List<Author> findAll();

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
