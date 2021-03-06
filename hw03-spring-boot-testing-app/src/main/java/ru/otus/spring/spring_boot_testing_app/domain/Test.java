package ru.otus.spring.spring_boot_testing_app.domain;

import ru.otus.spring.spring_boot_testing_app.domain.question.Question;

import java.util.List;

public class Test {
    private final String name;
    private final List<Question> questions;

    public Test(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int correctAnswersNumber() {
        return
            questions
                .stream()
                .mapToInt(Question::correctAnswersNumber)
                .sum()
        ;
    }
}
