package ru.otus.spring.spring_boot_testing_app.analyzer;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.spring.spring_boot_testing_app.domain.answer.CorrectAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.answer.IncorrectAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.answer.PossibleAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.question.AnsweredQuestion;
import ru.otus.spring.spring_boot_testing_app.domain.question.Question;
import ru.otus.spring.spring_boot_testing_app.service.analyzer.AnsweredQuestionAnalyzer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class AnsweredQuestionAnalyzerTest {
    private final static Question QUESTION_WITH_SINGLE_CORRECT_ANSWER =
        new Question("2x2=", Map.of(1, new IncorrectAnswer("3"), 2, new CorrectAnswer("4")));

    private final static Map<List<PossibleAnswer>, Integer> QUESTION_WITH_SINGLE_ANSWER_EXPECTED_POINTS =
        Map.of(
            emptyList(), 0,
            singletonList(new CorrectAnswer("4")), 1,
            singletonList(new IncorrectAnswer("3")), 0
        );

    private final static Question QUESTION_WITH_MULTIPLE_CORRECT_ANSWERS =
        new Question(
            "Scripting languages are ...",
            Map.of(
                1, new CorrectAnswer("PHP"),
                2, new IncorrectAnswer("Java"),
                3, new IncorrectAnswer("Esperanto"),
                4, new CorrectAnswer("Perl")
            )
        );

    private final static Map<List<PossibleAnswer>, Integer> QUESTION_WITH_MULTIPLE_CORRECT_ANSWERS_EXPECTED_POINTS =
        Map.of(
            emptyList(), 0,
            singletonList(new CorrectAnswer("PHP")), 1,
            singletonList(new IncorrectAnswer("Java")), 0,
            // all answers are correct -> 2
            Arrays.asList(new CorrectAnswer("PHP"), new CorrectAnswer("Perl")), 2,
            // two correct ones and one incorrect: 2 - 1 -> 1
            Arrays.asList(
                new CorrectAnswer("PHP"),
                new CorrectAnswer("Perl"),
                new IncorrectAnswer("Java")
            ), 1,
            // one correct and two incorrect ones : 1 - 2 = -1 < 0 -> 0
            Arrays.asList(
                new CorrectAnswer("PHP"),
                new IncorrectAnswer("Esperanto"),
                new IncorrectAnswer("Java")
            ), 0
        );

    @Test
    public void questionWithSingleCorrectAnswer() {
        var analyzer = new AnsweredQuestionAnalyzer();

        QUESTION_WITH_SINGLE_ANSWER_EXPECTED_POINTS.forEach(
            (answers, expectedCount) ->
                Assert.assertEquals(
                    expectedCount,
                    Integer.valueOf(
                        analyzer.countPoints(new AnsweredQuestion(QUESTION_WITH_SINGLE_CORRECT_ANSWER, answers))
                    )
                )
        );
    }

    @Test
    public void questionWithMultipleCorrectAnswers() {
        var analyzer = new AnsweredQuestionAnalyzer();

        QUESTION_WITH_MULTIPLE_CORRECT_ANSWERS_EXPECTED_POINTS.forEach(
            (answers, expectedCount) ->
                Assert.assertEquals(
                    expectedCount,
                    Integer.valueOf(
                        analyzer.countPoints(new AnsweredQuestion(QUESTION_WITH_MULTIPLE_CORRECT_ANSWERS, answers))
                    )
                )
        );
    }
}
