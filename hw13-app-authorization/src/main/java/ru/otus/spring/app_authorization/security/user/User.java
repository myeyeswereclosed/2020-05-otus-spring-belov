package ru.otus.spring.app_authorization.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @NotBlank
    private String firstName;

    private String lastName;

    private String login;
    private String password;

    @NotBlank
    private Role role = Role.USER;
}
