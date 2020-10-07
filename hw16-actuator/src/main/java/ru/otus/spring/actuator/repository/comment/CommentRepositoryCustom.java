package ru.otus.spring.actuator.repository.comment;

import org.springframework.data.repository.query.Param;
import ru.otus.spring.actuator.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Comment> update(Comment comment);

    Optional<String> delete(String id);

    void updateWithConfig(UpdateCommentConfig config);

    List<Comment> findShortByBook_Id(@Param("id") String bookId);
}
