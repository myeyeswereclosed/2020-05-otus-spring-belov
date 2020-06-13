package ru.otus.spring.students_testing.service.analyzer;

import ru.otus.spring.students_testing.domain.StudentTest;
import ru.otus.spring.students_testing.domain.TestResult;

public interface TestAnalyzer {
    TestResult analyze(StudentTest testMade);
}
