package ru.otus.spring.jpa_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.spring.jpa_book_info_app.domain.Author;

@AllArgsConstructor
@Getter
public class BookAuthor {
    private long authorId;
    private String authorFirstName;
    private String authorLastName;
    private long bookId;

    public Author toAuthor() {
        return new Author(authorId, authorFirstName, authorLastName);
    }
}
