package ru.otus.spring.students_testing.domain.answer;

public class CorrectAnswer extends PossibleAnswer {
    public CorrectAnswer(String text) {
        super(text);
    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public String description() {
        return "correct";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CorrectAnswer && ((CorrectAnswer) obj).getText().equals(text);
    }
}
