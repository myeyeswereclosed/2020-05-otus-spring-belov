package ru.otus.spring.students_testing.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.students_testing.service.analyzer.TestAnalyzer;
import ru.otus.spring.students_testing.service.analyzer.TestAnalyzerImpl;
import ru.otus.spring.students_testing.service.parser.TestSourceParser;
import ru.otus.spring.students_testing.service.printer.TestResultPrinter;
import ru.otus.spring.students_testing.service.runner.TestRunner;

@Service
public class TestSystem {
    private final TestSourceParser parser;
    private final TestRunner testRunner;
    private final TestAnalyzer analyzer;
    private final TestResultPrinter resultPrinter;

    public TestSystem(
        TestSourceParser parser,
        TestRunner testRunner,
        TestAnalyzer analyzer,
        TestResultPrinter resultPrinter
    ) {
        this.parser = parser;
        this.testRunner = testRunner;
        this.analyzer = analyzer;
        this.resultPrinter = resultPrinter;
    }

    public void run() {
        parser
            .parse()
            .flatMap(testRunner::run)
            .ifPresentOrElse(
                test -> resultPrinter.print(analyzer.analyze(test)),
                () -> System.out.println("Some error occurred")
            );
    }
}
