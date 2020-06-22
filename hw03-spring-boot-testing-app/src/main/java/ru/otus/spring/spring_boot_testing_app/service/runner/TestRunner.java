package ru.otus.spring.spring_boot_testing_app.service.runner;

import ru.otus.spring.spring_boot_testing_app.domain.StudentTest;
import ru.otus.spring.spring_boot_testing_app.domain.Test;

import java.util.Optional;

public interface TestRunner {
    Optional<StudentTest> run(Test test);
}
