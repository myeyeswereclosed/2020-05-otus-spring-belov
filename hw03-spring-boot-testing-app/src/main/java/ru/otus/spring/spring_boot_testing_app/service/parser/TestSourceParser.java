package ru.otus.spring.spring_boot_testing_app.service.parser;

import ru.otus.spring.spring_boot_testing_app.domain.Test;

import java.util.Optional;

public interface TestSourceParser {
    Optional<Test> parse();
}
