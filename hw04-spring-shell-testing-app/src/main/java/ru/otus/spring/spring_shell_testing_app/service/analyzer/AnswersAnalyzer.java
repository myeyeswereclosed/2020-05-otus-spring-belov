package ru.otus.spring.spring_shell_testing_app.service.analyzer;

import ru.otus.spring.spring_shell_testing_app.domain.question.AnsweredQuestion;

public interface AnswersAnalyzer {
    int countPoints(AnsweredQuestion answeredQuestion);
}
