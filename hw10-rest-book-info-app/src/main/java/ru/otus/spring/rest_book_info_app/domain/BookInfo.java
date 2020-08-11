package ru.otus.spring.rest_book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class BookInfo {
    private final Book book;
    private final List<Comment> comments;
}
