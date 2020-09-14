package ru.otus.spring.app_authorization.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.otus.spring.app_authorization.domain.Book;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Book> update(Book book) {
        return
            Optional.ofNullable(
                mongoTemplate.findAndModify(
                    new Query(Criteria.where("id").is(book.getId())),
                    new Update().set("title", book.getTitle()),
                    Book.class
                )
            );
    }

    @Override
    public Optional<String> delete(String id) {
        return
            Optional.ofNullable(
                mongoTemplate.findAndRemove(
                    new Query(Criteria.where("id").is(id)),
                    Book.class
                )
            ).map(Book::getId);
    }
}
