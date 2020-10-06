package ru.otus.spring.actuator.repository.comment;

import ru.otus.spring.actuator.domain.Comment;

import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Comment> update(Comment comment);

    Optional<String> delete(String id);

    void updateWithConfig(UpdateCommentConfig config);
}
