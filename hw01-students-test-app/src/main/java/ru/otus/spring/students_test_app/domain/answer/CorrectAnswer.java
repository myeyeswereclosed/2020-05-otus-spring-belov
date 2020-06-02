package ru.otus.spring.students_test_app.domain.answer;

public class CorrectAnswer extends PossibleAnswer {
    public CorrectAnswer(String text) {
        super(text);
    }

    @Override
    public boolean isCorrect() {
        return true;
    }
}
