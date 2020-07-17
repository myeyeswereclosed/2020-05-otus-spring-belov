package ru.otus.spring.hw07_apring_data_jpa_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookGenre {
    private int genreId;
    private String genreName;
    private long bookId;
}
