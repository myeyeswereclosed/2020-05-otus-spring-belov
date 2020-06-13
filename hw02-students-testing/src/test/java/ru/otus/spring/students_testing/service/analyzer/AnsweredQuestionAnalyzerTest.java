package ru.otus.spring.students_testing.service.analyzer;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.spring.students_testing.domain.answer.CorrectAnswer;
import ru.otus.spring.students_testing.domain.answer.IncorrectAnswer;
import ru.otus.spring.students_testing.domain.answer.PossibleAnswer;
import ru.otus.spring.students_testing.domain.question.AnsweredQuestion;
import ru.otus.spring.students_testing.domain.question.Question;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnsweredQuestionAnalyzerTest {

    private final static Question QUESTION_WITH_SINGLE_CORRECT_ANSWER =
        new Question("2x2=", Map.of(1, new IncorrectAnswer("3"), 2, new CorrectAnswer("4")));

    private final static Map<List<PossibleAnswer>, Integer> QUESTION_WITH_SINGLE_ANSWER_EXPECTED_POINTS =
        Map.of(
            Collections.emptyList(), 0,
            Collections.singletonList(new CorrectAnswer("4")), 1,
            Collections.singletonList(new IncorrectAnswer("3")), 0
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
            Collections.emptyList(), 0,
            Collections.singletonList(new CorrectAnswer("PHP")), 1,
            Collections.singletonList(new IncorrectAnswer("Java")), 0,
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
                    Integer.valueOf(analyzer.countPoints(new AnsweredQuestion(QUESTION_WITH_SINGLE_CORRECT_ANSWER, answers)))
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
