package ru.otus.spring.students_testing.service.input;

import java.util.Optional;

public interface UserInputReader {
    Optional<String> read();
}
