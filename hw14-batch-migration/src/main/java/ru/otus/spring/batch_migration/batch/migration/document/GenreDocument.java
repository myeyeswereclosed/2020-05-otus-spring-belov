package ru.otus.spring.batch_migration.batch.migration.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.spring.batch_migration.batch.migration.WithName;

@Document(collection = "genre")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class GenreDocument implements WithName {
    @Id
    private String id;
    private String name;

    public GenreDocument(String name) {
        this.name = name;
    }
}
