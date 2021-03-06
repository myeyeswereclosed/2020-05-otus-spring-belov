package ru.otus.spring.app_authorization.service.book.info.add;

import ru.otus.spring.app_authorization.domain.Author;
import ru.otus.spring.app_authorization.domain.Book;
import ru.otus.spring.app_authorization.domain.Comment;
import ru.otus.spring.app_authorization.domain.Genre;
import ru.otus.spring.app_authorization.service.result.ServiceResult;

public interface AddBookInfoService {
    ServiceResult<Book> addBookAuthor(String bookId, Author author);

    ServiceResult<Book> addBookGenre(String bookId, Genre genre);

    ServiceResult<Comment> addComment(String bookId, Comment comment);
}
