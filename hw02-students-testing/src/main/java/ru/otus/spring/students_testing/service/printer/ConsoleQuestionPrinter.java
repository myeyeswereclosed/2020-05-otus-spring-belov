package ru.otus.spring.students_testing.service.printer;

import org.springframework.stereotype.Service;
import ru.otus.spring.students_testing.domain.question.Question;

@Service
public class ConsoleQuestionPrinter implements QuestionPrinter {
    private final static String NEW_LINE = "\r\n";

    @Override
    public void print(Question question) {
        var output = new StringBuilder();
        var possibleAnswers = question.getPossibleAnswers();

        output
            .append(tab(1))
            .append(question.getText())
            .append(':');

        possibleAnswers.forEach(
            (index, answer) ->
                output
                    .append(tab(1))
                    .append(index)
                    .append(". ")
                    .append(answer.getText())
        );

        output.append(tab(1));

        System.out.println(output.toString());
    }


    private String tab(int times) {
        return NEW_LINE + "\t".repeat(Math.max(0, times));
    }
}
