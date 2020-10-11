package ru.otus.spring.app_container.repository.genre;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.app_container.domain.Genre;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface GenreRepository extends MongoRepository<Genre, String>, GenreRepositoryCustom {
    @RestResource(path = "genres", rel = "genres")
    List<Genre> findAll();

    Optional<Genre> findByName(String name);

}
