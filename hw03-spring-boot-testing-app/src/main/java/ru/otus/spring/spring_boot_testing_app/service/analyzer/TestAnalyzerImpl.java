package ru.otus.spring.spring_boot_testing_app.service.analyzer;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_boot_testing_app.config.Config;
import ru.otus.spring.spring_boot_testing_app.domain.StudentTest;
import ru.otus.spring.spring_boot_testing_app.domain.TestFailed;
import ru.otus.spring.spring_boot_testing_app.domain.TestPassed;
import ru.otus.spring.spring_boot_testing_app.domain.TestResult;

@Service
public class TestAnalyzerImpl implements TestAnalyzer {
    private final AnswersAnalyzer questionAnalyzer;
    private final int pointsToPass;

    public TestAnalyzerImpl(AnswersAnalyzer questionAnalyzer, Config properties) {
        this.questionAnalyzer = questionAnalyzer;
        this.pointsToPass = properties.getPointsToPass();
    }

    public TestResult analyze(StudentTest testMade) {
        var points = countPoints(testMade);

        return
            points >= pointsToPass
                ? new TestPassed(testMade, points)
                : new TestFailed(testMade, points)
        ;
    }

    private int countPoints(StudentTest test) {
        return
            test
                .getAnsweredQuestions()
                .stream()
                .mapToInt(questionAnalyzer::countPoints)
                .sum()
            ;
    }
}
