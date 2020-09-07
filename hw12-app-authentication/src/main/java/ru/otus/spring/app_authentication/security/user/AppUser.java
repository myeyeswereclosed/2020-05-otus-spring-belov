package ru.otus.spring.app_authentication.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    private String id;

    private String login;
    private String passwordEncrypted;

    private String firstName;
    private String lastName;

    public AppUser(String login, String passwordEncrypted, String firstName, String lastName) {
        this.login = login;
        this.passwordEncrypted = passwordEncrypted;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
