package ru.otus.spring.students_test_app.domain.answer;

abstract public class PossibleAnswer implements Answer {
    private final String text;

    PossibleAnswer(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}
