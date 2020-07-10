package ru.otus.spring.book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class Book {
    public Book(String title) {
        this.title = title;
    }

    public Book(long id, String title) {
        this(title);
        this.id = id;
    }

    private long id;
    private String title;
    private List<Author> authors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
}
