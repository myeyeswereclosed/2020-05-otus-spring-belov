package ru.otus.spring.students_testing.domain.answer;

abstract public class PossibleAnswer {
    final String text;

    PossibleAnswer(String text) {
        this.text = text;
    }

    abstract public boolean isCorrect();

    abstract public String description();

    public String getText() {
        return text;
    }
}
