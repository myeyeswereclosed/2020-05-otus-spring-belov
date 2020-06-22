package ru.otus.spring.spring_boot_testing_app.service.input;

import java.util.Optional;

public interface UserInputReader {
    Optional<String> read();
}
