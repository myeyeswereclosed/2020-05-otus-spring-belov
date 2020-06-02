package ru.otus.spring.students_test_app.service.printer;

import ru.otus.spring.students_test_app.domain.Test;

import java.util.stream.Collectors;

public class ConsoleTestPrinter implements TestPrinter {
    private final static String NEW_LINE = "\r\n";

    @Override
    public void print(Test test) {
        var output = new StringBuilder();

        output
            .append("Students test")
            .append(tab(1))
        ;

        var questions = test.getQuestions();
        var questionsNumber = questions.size();

        for (var i = 0; i < questionsNumber; i++) {
            var question = questions.get(i);
            output
                .append(i + 1)
                .append(". ")
                .append(question.getText())
                .append(':')
                .append(tab(2))
                .append(
                    question
                        .getPossibleAnswers()
                        .stream()
                        .map(answer -> " - " + answer.getText())
                        .collect(Collectors.joining(tab(2)))
                )
                .append(tab(1));
        }

        System.out.println(output.toString());
    }

    private String tab(int times) {
        return NEW_LINE + "\t".repeat(Math.max(0, times));
    }
}
