package ru.otus.spring.spring_boot_testing_app.analyzer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.otus.spring.spring_boot_testing_app.config.Config;
import ru.otus.spring.spring_boot_testing_app.domain.StudentTest;
import ru.otus.spring.spring_boot_testing_app.domain.answer.CorrectAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.question.AnsweredQuestion;
import ru.otus.spring.spring_boot_testing_app.domain.question.Question;
import ru.otus.spring.spring_boot_testing_app.domain.student.Student;
import ru.otus.spring.spring_boot_testing_app.service.analyzer.AnswersAnalyzer;
import ru.otus.spring.spring_boot_testing_app.service.analyzer.TestAnalyzerImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@RunWith(MockitoJUnitRunner.class)
public class TestAnalyzerTest {
    private final static int POINTS_TO_PASS = 1;
    private final static AnsweredQuestion QUESTION =
        new AnsweredQuestion(
            new Question("?", Map.of(1, new CorrectAnswer("!"))),
            emptyList()
        );
    private final static StudentTest STUDENT_TEST_STUB =
        new StudentTest(
            new Student("John", "Doe"),
            new ru.otus.spring.spring_boot_testing_app.domain.Test("Mock test", Collections.emptyList()),
            singletonList(QUESTION)
        );

    private List<Integer> successfulPointsList = List.of(POINTS_TO_PASS, POINTS_TO_PASS + 1);

    @Mock
    private AnswersAnalyzer analyzerMock;

    @Mock
    private Config configMock;

    @Test
    public void testPassed() {
        Mockito.when(configMock.getPointsToPass()).thenReturn(POINTS_TO_PASS);

        successfulPointsList.forEach(
            pointsMade -> {
                Mockito.when(analyzerMock.countPoints(QUESTION)).thenReturn(pointsMade);

                var analyzer = new TestAnalyzerImpl(analyzerMock, configMock);

                var result = analyzer.analyze(STUDENT_TEST_STUB);

                Assert.assertTrue(result.isPassed());
            }
        );
    }

    @Test
    public void testFailed() {
        Mockito.when(configMock.getPointsToPass()).thenReturn(POINTS_TO_PASS);
        Mockito.when(analyzerMock.countPoints(QUESTION)).thenReturn(POINTS_TO_PASS - 1);

        var analyzer = new TestAnalyzerImpl(analyzerMock, configMock);

        var result = analyzer.analyze(STUDENT_TEST_STUB);

        Assert.assertFalse(result.isPassed());
    }
}
