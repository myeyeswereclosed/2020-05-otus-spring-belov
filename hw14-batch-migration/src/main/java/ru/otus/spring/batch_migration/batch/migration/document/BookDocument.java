package ru.otus.spring.batch_migration.batch.migration.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookDocument {
    @Id
    private String id;

    private String title;

    private List<AuthorDocument> authors;
    private List<GenreDocument> genres;

    public BookDocument(String title, List<AuthorDocument> authors, List<GenreDocument> genres) {
        this.title = title;
        this.authors = authors;
        this.genres = genres;
    }


}
