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
@SqlResultSetMapping(
    name = "BookAuthorMapping",
    classes =
    @ConstructorResult(
        targetClass = BookAuthor.class,
        columns = {
            @ColumnResult(name = "author_id", type = Long.class),
            @ColumnResult(name = "first_name"),
            @ColumnResult(name = "last_name"),
            @ColumnResult(name = "book_id", type = Long.class)
        }
    )
)
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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public static Author fromDto(BookAuthor bookAuthor) {
        return new Author(bookAuthor.getAuthorId(), bookAuthor.getAuthorFirstName(), bookAuthor.getAuthorLastName());
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean hasFirstAndLastName(String firstName, String lastName) {
        return
            Objects.nonNull(this.firstName)
                &&
            Objects.nonNull(this.lastName)
                &&
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
