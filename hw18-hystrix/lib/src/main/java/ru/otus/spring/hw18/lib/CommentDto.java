package ru.otus.spring.hw18.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String id;
    private String text;
    private String bookId;

    public CommentDto(String text, String bookId) {
        this.text = text;
        this.bookId = bookId;
    }
}
