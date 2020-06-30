package ru.otus.spring.spring_shell_testing_app.service.formatter;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.domain.question.Question;

@Service
public class ConsoleQuestionFormatter implements QuestionFormatter {
    private final static String NEW_LINE = "\r\n";

    @Override
    public String format(Question question) {
        var result = new StringBuilder();
        var possibleAnswers = question.getPossibleAnswers();

        result
            .append(tab(1))
            .append(question.getText())
            .append(':');

        possibleAnswers.forEach(
            (index, answer) ->
                result
                    .append(tab(1))
                    .append(index)
                    .append(". ")
                    .append(answer.getText())
        );

        result.append(tab(1));

        return result.toString();
    }

    private String tab(int times) {
        return NEW_LINE + "\t".repeat(Math.max(0, times));
    }
}
