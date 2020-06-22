package ru.otus.spring.spring_boot_testing_app.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_boot_testing_app.service.analyzer.TestAnalyzer;
import ru.otus.spring.spring_boot_testing_app.service.formatter.TestResultFormatter;
import ru.otus.spring.spring_boot_testing_app.service.parser.TestSourceParser;
import ru.otus.spring.spring_boot_testing_app.service.informer.Informer;
import ru.otus.spring.spring_boot_testing_app.service.runner.TestRunner;

@Service
public class TestSystem {
    private final TestSourceParser parser;
    private final TestRunner testRunner;
    private final TestAnalyzer analyzer;
    private final TestResultFormatter formatter;
    private final Informer informer;

    public TestSystem(
        TestSourceParser parser,
        TestRunner testRunner,
        TestAnalyzer analyzer,
        TestResultFormatter formatter,
        Informer informer
    ) {
        this.parser = parser;
        this.testRunner = testRunner;
        this.analyzer = analyzer;
        this.formatter = formatter;
        this.informer = informer;
    }

    public void run() {
        parser
            .parse()
            .flatMap(testRunner::run)
            .ifPresentOrElse(
                test -> informer.inform(formatter.format(analyzer.analyze(test))),
                informer::errorMessage
            );
    }
}
