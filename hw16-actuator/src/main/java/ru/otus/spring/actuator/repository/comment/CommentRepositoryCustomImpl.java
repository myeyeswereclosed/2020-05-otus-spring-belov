package ru.otus.spring.actuator.repository.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.actuator.domain.Comment;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final MongoTemplate mongoTemplate;

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

    public void updateWithConfig(UpdateCommentConfig config) {
        mongoTemplate
            .updateMulti(
                new Query(Criteria.where(config.getField()).is(config.getValue())),
                config.getUpdate(),
                Comment.class
        );
    }

    // emulate just beginning of comment
    @Override
    public List<Comment> findShortByBook_Id(String bookId) {
        return
            mongoTemplate
                .find(
                    new Query(Criteria.where("book_id").is(bookId)),
                    Comment.class
                )
                .stream()
                .map(found -> new Comment(found.getId(), found.getText().substring(0, 5)))
                .collect(toList());
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
