package ru.otus.spring.hw18.book_service.service.comment;

import ru.otus.spring.hw18.book_service.domain.Book;
import ru.otus.spring.hw18.book_service.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> aboutBook(Book book);

    Comment add(Comment comment);
}