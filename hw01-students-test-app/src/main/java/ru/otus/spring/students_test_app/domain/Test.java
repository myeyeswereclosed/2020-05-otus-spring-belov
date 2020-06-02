package ru.otus.spring.students_test_app.domain;

import ru.otus.spring.students_test_app.domain.question.Question;

import java.util.List;

public interface Test {
    String getName();

    List<Question> getQuestions();
}
