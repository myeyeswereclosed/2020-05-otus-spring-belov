package ru.otus.spring.spring_integration.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    public String fullName() {
        return firstName + " " + lastName;
    }
}
