package ru.otus.spring.students_testing.service.input;

import ru.otus.spring.students_testing.domain.answer.PossibleAnswer;
import ru.otus.spring.students_testing.domain.student.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudentInputParser {
    Optional<Student> parseToStudent();

    List<PossibleAnswer> parseAnswers(Map<Integer, PossibleAnswer> possibleAnswers);
}
