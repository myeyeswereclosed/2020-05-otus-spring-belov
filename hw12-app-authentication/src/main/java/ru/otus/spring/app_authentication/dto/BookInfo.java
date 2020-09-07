package ru.otus.spring.app_authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.otus.spring.app_authentication.domain.Book;
import ru.otus.spring.app_authentication.domain.Comment;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class BookInfo {
    private final Book book;
    private final List<Comment> comments;
}
