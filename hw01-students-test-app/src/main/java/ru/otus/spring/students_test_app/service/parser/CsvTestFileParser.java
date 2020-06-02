package ru.otus.spring.students_test_app.service.parser;

import com.google.common.collect.Lists;
import ru.otus.spring.students_test_app.domain.StudentsTest;
import ru.otus.spring.students_test_app.domain.Test;
import ru.otus.spring.students_test_app.domain.answer.Answer;
import ru.otus.spring.students_test_app.domain.answer.CorrectAnswer;
import ru.otus.spring.students_test_app.domain.answer.IncorrectAnswer;
import ru.otus.spring.students_test_app.domain.question.Question;
import ru.otus.spring.students_test_app.domain.question.TestQuestion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CsvTestFileParser implements TestFileParser {
    private final static String DELIMITER = ",";
    private final static String TEST_NAME = "Students test";
    private final static int TEXT_AND_CORRECTNESS_LENGTH = 2;

    private final String fileName;

    public CsvTestFileParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Optional<Test> parse() {
        var inputStream = this.getClass().getResourceAsStream(fileName);
        var inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        var reader = new BufferedReader(inputReader);

        List<Question> questions = new ArrayList<>();

        try {
            for (String line; (line = reader.readLine()) != null;) {
                questions.add(makeQuestion(line));
            }

            return Optional.of(new StudentsTest(TEST_NAME, questions));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    private Question makeQuestion(String line) {
        var items = Arrays.asList(line.split(DELIMITER));

        var text = items.get(0);
        var availableAnswers = items.subList(1, items.size());

        List<Answer> answers =
            Lists
                .partition(availableAnswers, TEXT_AND_CORRECTNESS_LENGTH)
                .stream()
                .map(this::makeAnswer)
                .collect(Collectors.toList())
            ;

        return new TestQuestion(text, answers);
    }

    private Answer makeAnswer(List<String> items) {
        var text = items.get(0);
        var isCorrect = Boolean.parseBoolean(items.get(1));

        return isCorrect ? new CorrectAnswer(text) : new IncorrectAnswer(text);
    }
}
