package ru.otus.spring.students_test_app.domain.question;

import ru.otus.spring.students_test_app.domain.answer.Answer;

import java.util.List;

public interface Question {
    String getText();

    List<Answer> getPossibleAnswers();
}
