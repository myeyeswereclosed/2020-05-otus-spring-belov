package ru.otus.spring.mongo_db_book_info_app.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Author {
    @Id
    private String id;
    private String firstName;
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

    public String fullName() {
        return firstName + " " + lastName;
    }
}
