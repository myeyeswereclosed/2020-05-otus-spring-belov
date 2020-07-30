package ru.otus.spring.mongo_db_book_info_app.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
@Document
public class Genre {
    @Id
    private String id;

    @NotEmpty
    private String name;

    public Genre(String name) {
        this.name = name;
    }

    public boolean hasName(String name) {
        return Objects.nonNull(this.name) && this.name.equals(name);
    }
}
