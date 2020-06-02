package ru.otus.spring.students_test_app.domain;

import ru.otus.spring.students_test_app.domain.question.Question;

import java.util.List;

public class StudentsTest implements Test {
    private final String name;
    private final List<Question> questions;

    public StudentsTest(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
