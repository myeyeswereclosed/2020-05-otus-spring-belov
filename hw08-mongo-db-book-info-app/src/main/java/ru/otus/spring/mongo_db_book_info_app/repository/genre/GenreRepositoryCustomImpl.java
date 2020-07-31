package ru.otus.spring.mongo_db_book_info_app.repository.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;

import java.util.Optional;

@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Genre> update(Genre genre) {
        return
            Optional.ofNullable(
                mongoTemplate.findAndModify(
                    new Query(Criteria.where("id").is(genre.getId())),
                    new Update().set("name", genre.getName()),
                    Genre.class
                )
            );
    }

    @Override
    public Optional<String> delete(String id) {
        return
            Optional.ofNullable(
                mongoTemplate
                    .findAndRemove(
                        new Query(Criteria.where("id").is(id)),
                        Genre.class
                    )
            ).map(Genre::getId);
    }
}
