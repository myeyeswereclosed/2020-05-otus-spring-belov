package ru.otus.spring.spring_shell_testing_app.domain.answer;

public class CorrectAnswer extends PossibleAnswer {
    public CorrectAnswer(String text) {
        super(text);
    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CorrectAnswer && ((CorrectAnswer) obj).getText().equals(text);
    }
}
