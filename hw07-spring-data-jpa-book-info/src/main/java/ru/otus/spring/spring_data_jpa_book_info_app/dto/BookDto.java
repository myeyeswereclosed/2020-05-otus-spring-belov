package ru.otus.spring.spring_data_jpa_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Genre;

import java.util.List;

@AllArgsConstructor
@ToString
public class BookDto {
    private long id;
    private String title;
    private List<Author> authors;
    private List<Genre> genres;
}
