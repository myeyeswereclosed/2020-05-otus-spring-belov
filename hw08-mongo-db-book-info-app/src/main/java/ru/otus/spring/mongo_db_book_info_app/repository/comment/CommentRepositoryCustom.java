package ru.otus.spring.mongo_db_book_info_app.repository.comment;

import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;

import java.util.Optional;

public interface CommentRepositoryCustom {
    void updateForBook(Book book);

    Optional<Comment> update(Comment comment);

    Optional<String> delete(String id);
}
