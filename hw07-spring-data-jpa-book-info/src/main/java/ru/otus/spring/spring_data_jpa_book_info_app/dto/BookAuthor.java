package ru.otus.spring.spring_data_jpa_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookAuthor {
    private long authorId;
    private String authorFirstName;
    private String authorLastName;
    private long bookId;
}
