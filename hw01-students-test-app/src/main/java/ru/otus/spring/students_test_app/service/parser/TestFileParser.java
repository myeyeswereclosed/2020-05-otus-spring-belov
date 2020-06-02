package ru.otus.spring.students_test_app.service.parser;

import ru.otus.spring.students_test_app.domain.Test;

import java.util.Optional;

public interface TestFileParser {
    Optional<Test> parse();
}
