package ru.otus.spring.spring_boot_testing_app.domain.student;

import java.util.Objects;

public class Student {
    private final String firstName;
    private final String lastName;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public boolean hasFirstName(String firstName) {
        return Objects.nonNull(firstName) && firstName.equals(this.firstName);
    }

    public boolean hasLastName(String lastName) {
        return Objects.nonNull(lastName) && lastName.equals(this.lastName);
    }
}
