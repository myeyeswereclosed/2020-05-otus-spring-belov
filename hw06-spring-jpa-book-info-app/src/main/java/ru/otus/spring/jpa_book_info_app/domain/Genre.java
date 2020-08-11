package ru.otus.spring.jpa_book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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

    public boolean hasNoId() {
        return id == 0;
    }

    public boolean hasName(String name) {
        return Objects.nonNull(this.name) && this.name.equals(name);
    }
}
