package ru.otus.spring.spring_shell_testing_app.service.formatter;

import ru.otus.spring.spring_shell_testing_app.domain.question.Question;

public interface QuestionFormatter {
    String format(Question question);
}
