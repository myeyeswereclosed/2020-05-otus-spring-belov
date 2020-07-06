package ru.otus.spring.book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BookInfo {
    private final Book book;
    private List<Author> authors;
    private List<Genre> genres;
}
