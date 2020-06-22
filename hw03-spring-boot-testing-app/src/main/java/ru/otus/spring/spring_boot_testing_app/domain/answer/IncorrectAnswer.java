package ru.otus.spring.spring_boot_testing_app.domain.answer;

public class IncorrectAnswer extends PossibleAnswer {
    public IncorrectAnswer(String text) {
        super(text);
    }

    @Override
    public boolean isCorrect() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IncorrectAnswer && ((IncorrectAnswer) obj).getText().equals(text);
    }
}
