package ru.otus.spring.spring_boot_testing_app.service.formatter;

import ru.otus.spring.spring_boot_testing_app.domain.TestResult;

public interface TestResultFormatter {
    String format(TestResult result);
}
