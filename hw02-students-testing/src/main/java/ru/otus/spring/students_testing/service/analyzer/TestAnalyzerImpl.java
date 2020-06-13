package ru.otus.spring.students_testing.service.analyzer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.otus.spring.students_testing.domain.StudentTest;
import ru.otus.spring.students_testing.domain.TestFailed;
import ru.otus.spring.students_testing.domain.TestPassed;
import ru.otus.spring.students_testing.domain.TestResult;

@Service
@PropertySource("classpath:application.properties")
public class TestAnalyzerImpl implements TestAnalyzer {
    private final AnswersAnalyzer questionAnalyzer;
    private final int pointsToPass;

    TestAnalyzerImpl(
        AnswersAnalyzer questionAnalyzer,
        @Value("${test.points_for_passing}") int pointsToPass
    ) {
        this.questionAnalyzer = questionAnalyzer;
        this.pointsToPass = pointsToPass;
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
