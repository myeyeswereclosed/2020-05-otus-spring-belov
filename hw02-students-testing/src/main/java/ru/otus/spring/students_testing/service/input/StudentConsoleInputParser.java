package ru.otus.spring.students_testing.service.input;

import org.springframework.stereotype.Service;
import ru.otus.spring.students_testing.domain.answer.PossibleAnswer;
import ru.otus.spring.students_testing.domain.student.Student;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentConsoleInputParser implements StudentInputParser {
    private final static String WHITESPACE = " ";

    private final static String COMMA = ",";

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

                        if (studentsDataIsValid(inputSplitted)) {
                            return Optional.of(new Student(inputSplitted.get(0), inputSplitted.get(1)));
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
                .orElse(Collections.emptyList())
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
        } catch (NumberFormatException ignored) {

        }

        return Optional.empty();
    }

    private boolean studentsDataIsValid(List<String> data) {
        if (data.size() < 2 || data.stream().anyMatch(String::isEmpty)) {
            System.out.println("Sorry, your data is not correct");

            return false;
        }

        return true;
    }

}
