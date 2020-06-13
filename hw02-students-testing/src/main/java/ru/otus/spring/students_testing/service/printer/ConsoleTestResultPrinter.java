package ru.otus.spring.students_testing.service.printer;

import org.springframework.stereotype.Service;
import ru.otus.spring.students_testing.domain.TestResult;
import ru.otus.spring.students_testing.domain.answer.PossibleAnswer;
import ru.otus.spring.students_testing.domain.question.AnsweredQuestion;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleTestResultPrinter implements TestResultPrinter {
    @Override
    public void print(TestResult result) {
        var points = result.getPoints();

        var pointsOutput = points == 1 ? "point" : "points";

        var output =
            new StringBuilder(result.getTestMade().getStudent().toString())
                .append(", test is finished. Your result is ")
                .append(result.getPoints())
                .append(" ")
                .append(pointsOutput)
                .append(" of maximum ")
                .append(result.getTestMade().getTest().correctAnswersNumber())
                .append(". ")
                .append(result.status())
                .append("\r\nYour answers were: \r\n")
                .append(
                    result
                        .getTestMade()
                        .getAnsweredQuestions()
                        .stream()
                        .map(this::answeredQuestionOutput)
                        .collect(Collectors.joining("\r\n"))
                )
        ;

        System.out.println(output.toString());
    }

    private String answeredQuestionOutput(AnsweredQuestion answeredQuestion) {
        return answeredQuestion.getQuestion().getText() + " - " + answersOutput(answeredQuestion.getChosenAnswers());
    }

    private String answersOutput(List<PossibleAnswer> answers) {
        return
            answers.isEmpty()
                ? "No answer"
                : answers
                    .stream()
                    .map(answer -> answer.getText() + '(' + answer.description() + ')')
                    .collect(Collectors.joining(", "))
            ;
    }
}
