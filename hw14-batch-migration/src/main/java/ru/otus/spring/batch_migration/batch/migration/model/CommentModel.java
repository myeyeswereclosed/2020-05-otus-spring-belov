package ru.otus.spring.batch_migration.batch.migration.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class CommentModel {
    private long bookId;
    private String text;

    public CommentModel(String text) {
        this.text = text;
    }
}
