package ru.otus.spring.app_authorization.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Author {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    public Author(@NotNull String firstName, @NotNull String lastName) {
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
