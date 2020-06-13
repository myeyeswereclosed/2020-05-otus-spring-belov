package ru.otus.spring.students_testing.service.analyzer;

import ru.otus.spring.students_testing.domain.question.AnsweredQuestion;

interface AnswersAnalyzer {
    int countPoints(AnsweredQuestion answeredQuestion);
}
