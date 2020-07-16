package ru.otus.spring.jpa_book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.jpa_book_info_app.dto.BookGenre;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@SqlResultSetMapping(
    name = "BookGenreMapping",
    classes =
    @ConstructorResult(
        targetClass = BookGenre.class,
        columns = {
            @ColumnResult(name = "genre_id", type = Integer.class),
            @ColumnResult(name = "name"),
            @ColumnResult(name = "book_id", type = Long.class)
        }
    )
)
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    public static Genre fromDto(BookGenre bookGenre) {
        return new Genre(bookGenre.getGenreId(), bookGenre.getGenreName());
    }

    public Genre(String name) {
        this.name = name;
    }

    public boolean hasNoId() {
        return id == 0;
    }

    public boolean hasName(String name) {
        return Objects.nonNull(this.name) && this.name.equals(name);
    }
}
