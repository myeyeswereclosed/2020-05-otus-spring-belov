package ru.otus.spring.batch_migration.batch.migration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.otus.spring.batch_migration.batch.migration.WithName;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class AuthorModel implements WithName {
    private long id;
    private String firstName;
    private String lastName;

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }
}
