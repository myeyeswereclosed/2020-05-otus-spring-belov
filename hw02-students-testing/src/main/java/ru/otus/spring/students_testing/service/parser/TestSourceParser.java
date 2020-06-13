package ru.otus.spring.students_testing.service.parser;

import ru.otus.spring.students_testing.domain.Test;

import java.util.Optional;

public interface TestSourceParser {
    Optional<Test> parse();
}
