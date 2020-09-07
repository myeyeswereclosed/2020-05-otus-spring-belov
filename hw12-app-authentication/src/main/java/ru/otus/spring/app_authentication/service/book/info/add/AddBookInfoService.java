package ru.otus.spring.app_authentication.service.book.info.add;

import ru.otus.spring.app_authentication.domain.Author;
import ru.otus.spring.app_authentication.domain.Book;
import ru.otus.spring.app_authentication.domain.Comment;
import ru.otus.spring.app_authentication.domain.Genre;
import ru.otus.spring.app_authentication.service.result.ServiceResult;

public interface AddBookInfoService {
    ServiceResult<Book> addBookAuthor(String bookId, Author author);

    ServiceResult<Book> addBookGenre(String bookId, Genre genre);

    ServiceResult<Comment> addComment(String bookId, Comment comment);
}
