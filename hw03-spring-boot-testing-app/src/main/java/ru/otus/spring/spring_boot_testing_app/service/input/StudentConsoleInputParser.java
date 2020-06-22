package ru.otus.spring.spring_boot_testing_app.service.input;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_boot_testing_app.domain.answer.PossibleAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.student.Student;
import ru.otus.spring.spring_boot_testing_app.infrastructure.AppLogger;
import ru.otus.spring.spring_boot_testing_app.infrastructure.AppLoggerFactory;

import static java.util.Collections.emptyList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class StudentConsoleInputParser implements StudentInputParser {
    private final static int FIRST_NAME_INDEX = 0;
    private final static int LAST_NAME_INDEX = 1;
    private final static String WHITESPACE = " ";
    private final static String COMMA = ",";

    private final static AppLogger logger = AppLoggerFactory.logger(StudentConsoleInputParser.class);

    private final UserInputReader reader;

    public StudentConsoleInputParser(UserInputReader reader) {
        this.reader = reader;
    }

    public Optional<Student> parseToStudent() {
        return
            reader
                .read()
                .flatMap(
                    input -> {
                        var inputSplitted = Arrays.asList(input.split(WHITESPACE));

                        if (studentDataIsValid(inputSplitted)) {
                            return
                                Optional.of(
                                    new Student(
                                        inputSplitted.get(FIRST_NAME_INDEX),
                                        inputSplitted.get(LAST_NAME_INDEX)
                                    )
                                );
                        }

                        return Optional.empty();
                    }
                );
    }

    public List<PossibleAnswer> parseAnswers(Map<Integer, PossibleAnswer> possibleAnswers)  {
        return
            reader
                .read()
                .map(
                    input -> Arrays
                        .stream(input.split(COMMA))
                        .limit(possibleAnswers.size())
                        .filter(choice -> isCorrectIndex(choice, possibleAnswers))
                        .map(choice -> index(choice).get())
                        .distinct()
                        .map(possibleAnswers::get)
                        .collect(Collectors.toList())
                )
                .orElse(emptyList())
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

    private boolean studentDataIsValid(List<String> data) {
        if (data.size() < 2 || data.stream().anyMatch(String::isEmpty)) {
            logger.getLogger().warn("Incorrect user data was provided: {}", data);

            return false;
        }

        return true;
    }

}
