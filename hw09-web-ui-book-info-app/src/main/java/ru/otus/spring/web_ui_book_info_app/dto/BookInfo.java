package ru.otus.spring.web_ui_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Comment;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class BookInfo {
    private final Book book;
    private final List<Comment> comments;
}
