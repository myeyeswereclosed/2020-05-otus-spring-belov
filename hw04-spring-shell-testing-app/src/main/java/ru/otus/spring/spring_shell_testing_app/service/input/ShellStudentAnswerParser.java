package ru.otus.spring.spring_shell_testing_app.service.input;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.config.Config;
import ru.otus.spring.spring_shell_testing_app.domain.answer.PossibleAnswer;
import ru.otus.spring.spring_shell_testing_app.infrastructure.AppLogger;
import ru.otus.spring.spring_shell_testing_app.infrastructure.AppLoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShellStudentAnswerParser implements StudentAnswerParser {
    private final String answersDelimiter;

    private final static AppLogger logger = AppLoggerFactory.logger(ShellStudentAnswerParser.class);

    public ShellStudentAnswerParser(Config config) {
        this.answersDelimiter = config.getAnswersDelimiter();
    }

    @Override
    public List<PossibleAnswer> parseAnswers(Map<Integer, PossibleAnswer> possibleAnswers, String input) {
        return
            Arrays
                .stream(input.split(answersDelimiter))
                .limit(possibleAnswers.size())
                .filter(choice -> isCorrectIndex(choice, possibleAnswers))
                .map(choice -> index(choice).get())
                .distinct()
                .map(possibleAnswers::get)
                .collect(Collectors.toList())
            ;
    }

    private boolean isCorrectIndex(String indexString, Map<Integer, PossibleAnswer> possibleAnswers) {
        return
            index(indexString)
                .map(possibleAnswers::containsKey)
                .orElse(false)
            ;
    }

    private Optional<Integer> index(String indexString) {
        try {
            return Optional.of(Integer.parseInt(indexString.trim()));
        } catch (NumberFormatException e) {
            logger.getLogger().warn("Incorrect answer index '{}' was provided by user", indexString);
        }

        return Optional.empty();
    }
}
