package ru.otus.spring.app_authorization.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

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

    private Role role;

    public AppUser(String login, String passwordEncrypted, String firstName, String lastName, Role role) {
        this.login = login;
        this.passwordEncrypted = passwordEncrypted;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
