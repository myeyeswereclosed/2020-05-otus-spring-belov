package ru.otus.spring.students_test_app.domain.question;

import ru.otus.spring.students_test_app.domain.answer.Answer;

import java.util.List;

public class TestQuestion implements Question {
    private final String text;
    private final List<Answer> possibleAnswers;

    public TestQuestion(String text, List<Answer> possibleAnswers) {
        this.text = text;
        this.possibleAnswers = possibleAnswers;
    }

    public List<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public String getText() {
        return text;
    }
}
