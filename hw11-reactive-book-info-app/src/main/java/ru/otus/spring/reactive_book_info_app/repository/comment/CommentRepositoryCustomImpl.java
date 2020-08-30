package ru.otus.spring.reactive_book_info_app.repository.comment;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Comment> update(Comment comment) {
        return
            mongoTemplate.findAndModify(
                new Query(Criteria.where("id").is(comment.getId())),
                new Update().set("text", comment.getText()),
                Comment.class
            );
    }

    public Mono<UpdateResult> update(UpdateCommentConfig config) {
        return
            mongoTemplate
                .updateMulti(
                    new Query(Criteria.where(config.getField()).is(config.getValue())),
                    config.getUpdate(),
                    Comment.class
            );
    }

    @Override
    public Mono<String> delete(String id) {
        return
            mongoTemplate
                .findAndRemove(new Query(Criteria.where("id").is(id)), Comment.class)
                .map(Comment::getId);
    }
}
