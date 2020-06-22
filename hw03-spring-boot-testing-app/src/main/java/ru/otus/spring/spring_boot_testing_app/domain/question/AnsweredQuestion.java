package ru.otus.spring.spring_boot_testing_app.domain.question;

import ru.otus.spring.spring_boot_testing_app.domain.answer.PossibleAnswer;

import java.util.List;

public class AnsweredQuestion {
    private final Question question;
    private final List<PossibleAnswer> chosenAnswers;

    public AnsweredQuestion(Question question, List<PossibleAnswer> answers) {
        this.question = question;
        this.chosenAnswers = answers;
    }

    public Question getQuestion() {
        return question;
    }

    public List<PossibleAnswer> getChosenAnswers() {
        return chosenAnswers;
    }

}
