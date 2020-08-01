package ru.otus.spring.mongo_db_book_info_app.repository.author;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Author> update(Author author) {
        return
            Optional.ofNullable(
                mongoTemplate.findAndModify(
                    new Query(Criteria.where("id").is(author.getId())),
                    new Update()
                        .set("firstName", author.getFirstName())
                        .set("lastName", author.getLastName()),
                    Author.class
                )
            );
    }

    @Override
    public Optional<String> delete(String id) {
        return
            Optional.ofNullable(
                mongoTemplate.findAndRemove(
                    new Query(Criteria.where("id").is(id)),
                    Author.class
                )
            ).map(Author::getId);
    }
}
