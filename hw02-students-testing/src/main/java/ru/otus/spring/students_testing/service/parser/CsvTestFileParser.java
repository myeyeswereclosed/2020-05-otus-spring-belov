package ru.otus.spring.students_testing.service.parser;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.spring.students_testing.domain.Test;
import ru.otus.spring.students_testing.domain.answer.CorrectAnswer;
import ru.otus.spring.students_testing.domain.answer.IncorrectAnswer;
import ru.otus.spring.students_testing.domain.answer.PossibleAnswer;
import ru.otus.spring.students_testing.domain.question.Question;
import ru.otus.spring.students_testing.service.reader.FileReader;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application.properties")
public class CsvTestFileParser implements TestSourceParser {
    private final static String DELIMITER = ",";
    private final static String TEST_NAME = "Student test";
    // one question and at least two tuples of {possibleAnswer, correctnessFlag}
    private final static int LINE_ITEMS_MINIMUM = 5;
    private final static int TEXT_AND_CORRECTNESS_LENGTH = 2;

    private final FileReader fileReader;

    CsvTestFileParser(FileReader reader) {
        this.fileReader = reader;
    }

    @Override
    public Optional<Test> parse() {
        var lines = fileReader.read();

        return
            lines.isEmpty()
                ? Optional.empty()
                : makeTest(lines)
            ;
    }

    private Optional<Test> makeTest(List<String> questionLines) {
        var questionsParsed =
            questionLines
                .stream()
                .filter(line -> Objects.nonNull(line) && !StringUtils.isEmpty(line))
                .map(this::makeQuestion)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList())
        ;

        return
            allLinesSuccessfullyParsed(questionLines, questionsParsed)
                ? Optional.of(new Test(TEST_NAME, questionsParsed))
                : Optional.empty()
        ;
    }

    private boolean allLinesSuccessfullyParsed(List<String> fileLines, List<Question> questions) {
        return questions.size() == fileLines.size();
    }

    private Optional<Question> makeQuestion(String line) {
        var items = Arrays.asList(line.split(DELIMITER));

        if (items.isEmpty() || incorrectQuestionAndAnswersNumber(items)) {
            return Optional.empty();
        }

        var text = items.get(0);
        var availableAnswers = items.subList(1, items.size());

        List<PossibleAnswer> answers =
            Lists
                .partition(availableAnswers, TEXT_AND_CORRECTNESS_LENGTH)
                .stream()
                .map(this::makeAnswer)
                .collect(Collectors.toList())
            ;

        return
            Optional.of(
                new Question(
                    text,
                    answers
                        .stream()
                        .collect(
                            Collectors.toMap(
                                answer -> answers.indexOf(answer) + 1,
                                answer -> answer
                            )
                        )
                )
            );
    }

    private boolean incorrectQuestionAndAnswersNumber(List<String> lineItems) {
        var itemsNumber = lineItems.size();

        return itemsNumber < LINE_ITEMS_MINIMUM || itemsNumber % 2 == 0;
    }

    private PossibleAnswer makeAnswer(List<String> items) {
        var text = items.get(0);
        var isCorrect = Boolean.parseBoolean(items.get(1));

        return isCorrect ? new CorrectAnswer(text) : new IncorrectAnswer(text);
    }
}
