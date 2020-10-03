package ru.otus.spring.batch_migration.batch.migration.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class BookGenre {
    private long bookId;
    private long id;
    private String name;
}
