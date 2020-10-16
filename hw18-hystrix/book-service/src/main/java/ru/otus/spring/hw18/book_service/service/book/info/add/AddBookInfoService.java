package ru.otus.spring.hw18.book_service.service.book.info.add;

import ru.otus.spring.hw18.book_service.domain.Author;
import ru.otus.spring.hw18.book_service.domain.Book;
import ru.otus.spring.hw18.book_service.domain.Comment;
import ru.otus.spring.hw18.book_service.domain.Genre;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

public interface AddBookInfoService {
    ServiceResult<Book> addBookAuthor(String bookId, Author author);

    ServiceResult<Book> addBookGenre(String bookId, Genre genre);

    ServiceResult<Comment> addComment(String bookId, Comment comment);
}
