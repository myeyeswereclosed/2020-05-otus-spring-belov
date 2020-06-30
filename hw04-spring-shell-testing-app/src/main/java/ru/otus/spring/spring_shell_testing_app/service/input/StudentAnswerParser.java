package ru.otus.spring.spring_shell_testing_app.service.input;

import ru.otus.spring.spring_shell_testing_app.domain.answer.PossibleAnswer;

import java.util.List;
import java.util.Map;

public interface StudentAnswerParser {
    List<PossibleAnswer> parseAnswers(Map<Integer, PossibleAnswer> possibleAnswers, String input);
}
