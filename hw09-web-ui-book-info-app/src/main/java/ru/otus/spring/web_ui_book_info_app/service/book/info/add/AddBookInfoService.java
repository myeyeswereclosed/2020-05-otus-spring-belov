package ru.otus.spring.web_ui_book_info_app.service.book.info.add;

import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Comment;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;
import ru.otus.spring.web_ui_book_info_app.service.result.ServiceResult;

public interface AddBookInfoService {
    ServiceResult<Book> addBookAuthor(String bookId, Author author);

    ServiceResult<Book> addBookGenre(String bookId, Genre genre);

    ServiceResult<Comment> addComment(String bookId, Comment comment);
}
