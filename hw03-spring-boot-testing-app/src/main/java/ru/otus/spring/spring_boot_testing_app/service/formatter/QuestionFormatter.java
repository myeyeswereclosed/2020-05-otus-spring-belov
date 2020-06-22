package ru.otus.spring.spring_boot_testing_app.service.formatter;

import ru.otus.spring.spring_boot_testing_app.domain.question.Question;

public interface QuestionFormatter {
    String format(Question question);
}
