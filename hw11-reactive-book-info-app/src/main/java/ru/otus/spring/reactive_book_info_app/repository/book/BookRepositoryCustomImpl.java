package ru.otus.spring.reactive_book_info_app.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Book;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Book> update(Book book) {
        return
            mongoTemplate.findAndModify(
                new Query(Criteria.where("id").is(book.getId())),
                new Update().set("title", book.getTitle()),
                Book.class
            );
    }

    @Override
    public Mono<String> delete(String id) {
        return mongoTemplate.findAndRemove(new Query(Criteria.where("id").is(id)), Book.class).map(Book::getId);
    }
}
