package ru.otus.spring.rest_book_info_app.controller.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;

import java.util.List;

@AllArgsConstructor
@Data
public class BookDto {
    private final String id;
    private final String title;
    private final List<Author> authors;
    private final List<Genre> genres;

    public static BookDto fromBook(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthors(), book.getGenres());
    }
}
