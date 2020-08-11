package ru.otus.spring.rest_book_info_app.repository.comment;

import ru.otus.spring.rest_book_info_app.domain.Comment;

import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Comment> update(Comment comment);

    Optional<String> delete(String id);

    void update(UpdateCommentConfig config);
}
