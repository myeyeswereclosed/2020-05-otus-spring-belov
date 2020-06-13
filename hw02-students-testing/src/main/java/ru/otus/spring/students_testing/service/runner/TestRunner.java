package ru.otus.spring.students_testing.service.runner;

import ru.otus.spring.students_testing.domain.StudentTest;
import ru.otus.spring.students_testing.domain.Test;

import java.util.Optional;

public interface TestRunner {
    Optional<StudentTest> run(Test test);
}
