package ru.otus.spring.batch_migration.batch.migration.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDocument {
    @Id
    private String id;

    private String text;
    private BookDocument book;

    public CommentDocument(String text, BookDocument book) {
        this.text = text;
        this.book = book;
    }
}
