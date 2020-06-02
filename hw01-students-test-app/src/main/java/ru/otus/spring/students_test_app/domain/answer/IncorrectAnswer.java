package ru.otus.spring.students_test_app.domain.answer;

public class IncorrectAnswer extends PossibleAnswer {
    public IncorrectAnswer(String text) {
        super(text);
    }

    @Override
    public boolean isCorrect() {
        return false;
    }
}
