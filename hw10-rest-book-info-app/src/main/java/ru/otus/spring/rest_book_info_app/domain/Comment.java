package ru.otus.spring.rest_book_info_app.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class Comment {
    @Id
    private String id;

    @Setter
    private String text;

    @Setter
    private Book book;

    public Comment(String text) {
        this.text = text;
    }

    public Comment(String id, String text) {
        this(text);
        this.id = id;
    }

    public Comment(String text, Book book) {
        this(text);
        this.book = book;
    }
}
