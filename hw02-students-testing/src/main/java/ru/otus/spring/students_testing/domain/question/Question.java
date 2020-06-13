package ru.otus.spring.students_testing.domain.question;


import ru.otus.spring.students_testing.domain.answer.PossibleAnswer;

import java.util.Map;

public class Question {
    private final String text;
    private final Map<Integer, PossibleAnswer> possibleAnswers;

    public Question(String text, Map<Integer, PossibleAnswer> possibleAnswers) {
        this.text = text;
        this.possibleAnswers = possibleAnswers;
    }

    public Map<Integer, PossibleAnswer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public String getText() {
        return text;
    }

    public boolean hasMultipleCorrectAnswers() {
        return correctAnswersNumber() > 1;
    }

    public int correctAnswersNumber() {
        return
            (int) possibleAnswers
                .values()
                .stream()
                .filter(PossibleAnswer::isCorrect)
                .count()
        ;
    }
}
