package ru.otus.spring.app_container.service.book.info.add;

import ru.otus.spring.app_container.domain.Author;
import ru.otus.spring.app_container.domain.Book;
import ru.otus.spring.app_container.domain.Comment;
import ru.otus.spring.app_container.domain.Genre;
import ru.otus.spring.app_container.service.result.ServiceResult;

public interface AddBookInfoService {
    ServiceResult<Book> addBookAuthor(String bookId, Author author);

    ServiceResult<Book> addBookGenre(String bookId, Genre genre);

    ServiceResult<Comment> addComment(String bookId, Comment comment);
}
