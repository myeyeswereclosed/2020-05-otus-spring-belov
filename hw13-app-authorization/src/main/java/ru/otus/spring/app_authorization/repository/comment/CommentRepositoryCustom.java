package ru.otus.spring.app_authorization.repository.comment;

import ru.otus.spring.app_authorization.domain.Comment;

import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Comment> update(Comment comment);

    Optional<String> delete(String id);

    void update(UpdateCommentConfig config);
}
