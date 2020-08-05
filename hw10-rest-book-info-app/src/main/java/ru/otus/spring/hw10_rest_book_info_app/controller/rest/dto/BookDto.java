package ru.otus.spring.hw10_rest_book_info_app.controller.rest.dto;

import lombok.AllArgsConstructor;
import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;

import java.util.List;

@AllArgsConstructor
public class BookDto {
    private final String id;
    private final String title;
    private final List<Author> authors;
    private final List<Genre> genres;
}
