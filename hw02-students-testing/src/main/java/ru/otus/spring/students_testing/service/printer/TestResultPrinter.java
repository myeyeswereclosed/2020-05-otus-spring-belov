package ru.otus.spring.students_testing.service.printer;

import ru.otus.spring.students_testing.domain.TestResult;

public interface TestResultPrinter {
    void print(TestResult result);
}
