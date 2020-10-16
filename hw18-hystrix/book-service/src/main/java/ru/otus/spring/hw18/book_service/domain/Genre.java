package ru.otus.spring.hw18.book_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@ToString
@Document
public class Genre {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    private String name;

    public Genre(@NotNull String name) {
        this.name = name;
    }

    public boolean hasName(String name) {
        return Objects.nonNull(this.name) && this.name.equals(name);
    }
}
