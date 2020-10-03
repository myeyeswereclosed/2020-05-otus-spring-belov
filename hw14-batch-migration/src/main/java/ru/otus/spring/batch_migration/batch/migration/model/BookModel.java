package ru.otus.spring.batch_migration.batch.migration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class BookModel implements Serializable {
    private long id;
    private String title;
}
