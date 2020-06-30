package ru.otus.spring.spring_shell_testing_app.domain.answer;

abstract public class PossibleAnswer {
    final String text;

    PossibleAnswer(String text) {
        this.text = text;
    }

    abstract public boolean isCorrect();

    public String getText() {
        return text;
    }
}
