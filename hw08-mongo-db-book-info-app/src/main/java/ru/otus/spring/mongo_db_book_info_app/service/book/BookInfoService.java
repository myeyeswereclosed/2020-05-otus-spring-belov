package ru.otus.spring.mongo_db_book_info_app.service.book;


import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.dto.BookInfo;
import ru.otus.spring.mongo_db_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface BookInfoService {
    ServiceResult<Book> addBookAuthor(String bookId, Author author);

    ServiceResult<Book> addBookGenre(String bookId, Genre genre);

    ServiceResult<Comment> addComment(String bookId, Comment comment);

    ServiceResult<BookInfo> get(String bookId);

    ServiceResult<List<BookInfo>> getAll();
}
