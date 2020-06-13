package ru.otus.spring.students_testing.domain.answer;

public class IncorrectAnswer extends PossibleAnswer {
    public IncorrectAnswer(String text) {
        super(text);
    }

    @Override
    public boolean isCorrect() {
        return false;
    }

    @Override
    public String description() {
        return "incorrect";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IncorrectAnswer && ((IncorrectAnswer) obj).getText().equals(text);
    }
}
