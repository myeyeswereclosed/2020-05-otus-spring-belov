package ru.otus.spring.batch_migration.batch.migration.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.spring.batch_migration.batch.migration.WithName;

@Document(collection = "author")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AuthorDocument implements WithName {
    @Id
    private String id;
    private long sourceId;
    private String firstName;
    private String lastName;

    public AuthorDocument(long sourceId, String firstName, String lastName) {
        this.sourceId = sourceId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }
}
