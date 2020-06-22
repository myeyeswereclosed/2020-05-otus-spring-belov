package ru.otus.spring.spring_boot_testing_app.service.analyzer;

import ru.otus.spring.spring_boot_testing_app.domain.StudentTest;
import ru.otus.spring.spring_boot_testing_app.domain.TestResult;

public interface TestAnalyzer {
    TestResult analyze(StudentTest testMade);
}
