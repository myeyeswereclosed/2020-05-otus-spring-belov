package ru.otus.spring.mongo_db_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;

import java.util.List;

@AllArgsConstructor
@Getter
public class BookInfo {
    private final Book book;
    private final List<Comment> comments;
}
