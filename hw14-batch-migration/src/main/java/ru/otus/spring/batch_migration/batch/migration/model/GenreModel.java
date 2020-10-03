package ru.otus.spring.batch_migration.batch.migration.model;

import lombok.*;
import ru.otus.spring.batch_migration.batch.migration.WithName;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class GenreModel implements WithName {
    private int id;
    private String name;
}
