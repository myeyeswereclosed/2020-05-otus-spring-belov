package ru.otus.spring.app_authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.otus.spring.app_authorization.domain.Book;
import ru.otus.spring.app_authorization.domain.Comment;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class BookInfo {
    private final Book book;
    private final List<Comment> comments;
}
