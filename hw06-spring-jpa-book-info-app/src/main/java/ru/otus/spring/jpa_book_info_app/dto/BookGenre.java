package ru.otus.spring.jpa_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.spring.jpa_book_info_app.domain.Genre;

@AllArgsConstructor
@Getter
public class BookGenre {
    private int genreId;
    private String genreName;
    private long bookId;

    public Genre toGenre() {
        return new Genre(genreId, genreName);
    }
}
