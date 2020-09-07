package ru.otus.spring.app_authentication.repository.comment;

import org.springframework.stereotype.Repository;
import ru.otus.spring.app_authentication.domain.Comment;

import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Comment> update(Comment comment);

    Optional<String> delete(String id);

    void update(UpdateCommentConfig config);
}
