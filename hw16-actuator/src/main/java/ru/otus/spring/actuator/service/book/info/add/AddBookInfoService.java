package ru.otus.spring.actuator.service.book.info.add;

import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.domain.Book;
import ru.otus.spring.actuator.domain.Comment;
import ru.otus.spring.actuator.domain.Genre;
import ru.otus.spring.actuator.service.result.ServiceResult;

public interface AddBookInfoService {
    ServiceResult<Book> addBookAuthor(String bookId, Author author);

    ServiceResult<Book> addBookGenre(String bookId, Genre genre);

    ServiceResult<Comment> addComment(String bookId, Comment comment);
}
