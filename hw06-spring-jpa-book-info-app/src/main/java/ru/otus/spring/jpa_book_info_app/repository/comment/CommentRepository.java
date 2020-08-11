package ru.otus.spring.jpa_book_info_app.repository.comment;

import ru.otus.spring.jpa_book_info_app.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    void update(Comment comment);

    Optional<Comment> findById(long id);

    List<Comment> findByBook(long bookId);

    List<Comment> findAll();

    boolean delete(long id);
}
