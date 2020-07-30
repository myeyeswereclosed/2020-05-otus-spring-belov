package ru.otus.spring.mongo_db_book_info_app.repository.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;

import java.util.Optional;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateForBook(Book book) {
        mongoTemplate.updateMulti(
            new Query(Criteria.where("book._id").is(book.getId())),
            new Update().set("book.title", book.getTitle()),
            Comment.class
        );
    }

    @Override
    public Optional<Comment> update(Comment comment) {
        return
            Optional.ofNullable(
                mongoTemplate.findAndModify(
                    new Query(Criteria.where("id").is(comment.getId())),
                    new Update().set("text", comment.getText()),
                    Comment.class
                )
            );
    }

    @Override
    public Optional<String> delete(String id) {
        return
            Optional.ofNullable(
                mongoTemplate.findAndRemove(
                    new Query(Criteria.where("id").is(id)),
                    Comment.class
                )
            ).map(Comment::getId);
    }
}
