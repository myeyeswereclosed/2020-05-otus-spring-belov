package ru.otus.spring.spring_boot_testing_app.service.analyzer;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_boot_testing_app.domain.question.AnsweredQuestion;

@Service
public class AnsweredQuestionAnalyzer implements AnswersAnalyzer {
    private static final int CORRECT_ANSWER_POINT = 1;
    private static final int INCORRECT_ANSWER_POINT = 0;

    public int countPoints(AnsweredQuestion answeredQuestion) {
        return
            answeredQuestion.getQuestion().hasMultipleCorrectAnswers()
                ? forMultipleCorrectAnswers(answeredQuestion)
                : forSinglePossibleCorrectAnswer(answeredQuestion)
        ;
    }

    private int forSinglePossibleCorrectAnswer(AnsweredQuestion answeredQuestion) {
        return
            answeredQuestion
                .getChosenAnswers()
                .stream()
                .findFirst()
                .map(answer -> answer.isCorrect() ? CORRECT_ANSWER_POINT : INCORRECT_ANSWER_POINT)
                .orElse(0)
            ;
    }

    private int forMultipleCorrectAnswers(AnsweredQuestion answeredQuestion) {
        var result =
            answeredQuestion
                .getChosenAnswers()
                .stream()
                .mapToInt(answer -> answer.isCorrect() ? CORRECT_ANSWER_POINT : CORRECT_ANSWER_POINT * (-1))
                .sum()
            ;

        return Math.max(result, 0);
    }
}
