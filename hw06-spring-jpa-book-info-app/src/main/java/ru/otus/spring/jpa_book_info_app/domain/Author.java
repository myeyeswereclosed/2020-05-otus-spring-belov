package ru.otus.spring.jpa_book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.jpa_book_info_app.dto.BookAuthor;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
    name = "author",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"first_name", "last_name"})
    }
)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean hasFirstAndLastName(String firstName, String lastName) {
        return
            this.firstName.equals(firstName)
                &&
            this.lastName.equals(lastName)
        ;
    }

    public boolean hasNoId() {
        return id == 0;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
