package ru.otus.spring.spring_data_jpa_book_info_app.domain;

import lombok.*;
import ru.otus.spring.spring_data_jpa_book_info_app.dto.BookGenre;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    public Genre(String name) {
        this.name = name;
    }

    public boolean hasName(String name) {
        return Objects.nonNull(this.name) && this.name.equals(name);
    }
}
