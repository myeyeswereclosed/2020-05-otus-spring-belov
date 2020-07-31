package ru.otus.spring.mongo_db_book_info_app.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;

import java.util.Optional;

@RequiredArgsConstructor
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
    public void updateAuthor(Author author) {
        mongoTemplate
            .updateMulti(
                new Query(Criteria.where("authors.id").is(author.getId())),
                new Update()
                    .set("authors.$.firstName", author.getFirstName())
                    .set("authors.$.lastName", author.getLastName()),
                Book.class
            );
    }

    @Override
    public void removeAuthor(String authorId) {
        var query = new Query(Criteria.where("id").is(authorId));

        mongoTemplate
            .updateMulti(
                new Query(),
                new Update().pull("authors", query),
                Book.class
            );
    }

    @Override
    public void updateGenre(Genre genre) {
        mongoTemplate
            .updateMulti(
                new Query(Criteria.where("genres.id").is(genre.getId())),
                new Update().set("genres.$.name", genre.getName()),
                Book.class
            );
    }

    @Override
    public void removeGenre(String genreId) {
        var query = new Query(Criteria.where("id").is(genreId));

        mongoTemplate
            .updateMulti(
                new Query(),
                new Update().pull("genres", query),
                Book.class
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
