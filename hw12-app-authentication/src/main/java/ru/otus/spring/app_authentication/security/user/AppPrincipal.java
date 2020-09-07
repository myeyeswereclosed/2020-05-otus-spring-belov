package ru.otus.spring.app_authentication.security.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppPrincipal extends User {
    @Getter
    private final String firstName;

    @Getter
    private final String lastName;

    public AppPrincipal(
        String login,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String firstName,
        String lastName
    ) {
        super(login, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
