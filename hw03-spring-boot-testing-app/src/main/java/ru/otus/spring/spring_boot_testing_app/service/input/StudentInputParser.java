package ru.otus.spring.spring_boot_testing_app.service.input;

import ru.otus.spring.spring_boot_testing_app.domain.answer.PossibleAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.student.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudentInputParser {
    Optional<Student> parseToStudent();

    List<PossibleAnswer> parseAnswers(Map<Integer, PossibleAnswer> possibleAnswers);
}
