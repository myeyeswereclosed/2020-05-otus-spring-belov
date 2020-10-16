package ru.otus.spring.hw18.book_service.domain;

import lombok.*;
import ru.otus.spring.hw18.lib.CommentDto;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class Comment {
    private String id;

    @Setter
    private String text;

    @Setter
    private Book book;

    public static Comment fromDto(CommentDto dto, Book book) {
        return new Comment(dto.getId(), dto.getText(), book);
    }

    public static Comment fromText(String text) {
        return new Comment(text);
    }

    public Comment(String text) {
        this.text = text;
    }

    public CommentDto toDto() {
        return new CommentDto(id, text, book.getId());
    }
}
