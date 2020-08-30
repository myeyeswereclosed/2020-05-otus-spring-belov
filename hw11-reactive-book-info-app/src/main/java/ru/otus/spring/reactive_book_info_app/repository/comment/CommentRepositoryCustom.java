package ru.otus.spring.reactive_book_info_app.repository.comment;

import com.mongodb.client.result.UpdateResult;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Comment;

public interface CommentRepositoryCustom {
    Mono<Comment> update(Comment comment);

    Mono<String> delete(String id);

    Mono<UpdateResult> update(UpdateCommentConfig config);
}
