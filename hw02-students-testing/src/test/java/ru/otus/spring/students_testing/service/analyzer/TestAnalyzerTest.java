package ru.otus.spring.students_testing.service.analyzer;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.spring.students_testing.domain.StudentTest;
import ru.otus.spring.students_testing.domain.answer.CorrectAnswer;
import ru.otus.spring.students_testing.domain.question.AnsweredQuestion;
import ru.otus.spring.students_testing.domain.question.Question;
import ru.otus.spring.students_testing.domain.student.Student;

import java.util.Collections;
import java.util.Map;

public class TestAnalyzerTest {
    private final static int POINTS_TO_PASS = 1;
    private final static StudentTest STUDENT_TEST_STUB =
        new StudentTest(
            new Student("John", "Doe"),
            new ru.otus.spring.students_testing.domain.Test("Mock test", Collections.emptyList()),
            Collections.singletonList(
                new AnsweredQuestion(
                    new Question("?", Map.of(1, new CorrectAnswer("!"))),
                    Collections.emptyList()
                )
            )
        );

    @Test
    public void testPassed() {
        var analyzer = new TestAnalyzerImpl(new AnswersAnalyzerMock(POINTS_TO_PASS), POINTS_TO_PASS);

        var result = analyzer.analyze(STUDENT_TEST_STUB);

        Assert.assertTrue(result.isPassed());
    }

    @Test
    public void testFailed() {
        var analyzer = new TestAnalyzerImpl(new AnswersAnalyzerMock(POINTS_TO_PASS - 1), POINTS_TO_PASS);

        var result = analyzer.analyze(STUDENT_TEST_STUB);

        Assert.assertFalse(result.isPassed());
    }

    private static class AnswersAnalyzerMock implements AnswersAnalyzer {
        private final int points;

        AnswersAnalyzerMock(int points) {
            this.points = points;
        }

        @Override
        public int countPoints(AnsweredQuestion answeredQuestion) {
            return points;
        }
    }
}
