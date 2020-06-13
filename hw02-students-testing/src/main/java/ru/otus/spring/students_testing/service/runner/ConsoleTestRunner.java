package ru.otus.spring.students_testing.service.runner;

import org.springframework.stereotype.Controller;
import ru.otus.spring.students_testing.domain.StudentTest;
import ru.otus.spring.students_testing.domain.Test;
import ru.otus.spring.students_testing.domain.question.AnsweredQuestion;
import ru.otus.spring.students_testing.service.input.StudentInputParser;
import ru.otus.spring.students_testing.service.printer.Informer;
import ru.otus.spring.students_testing.service.printer.QuestionPrinter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ConsoleTestRunner implements TestRunner {
    private final StudentInputParser parser;
    private final QuestionPrinter printer;
    private final Informer informer;

    public ConsoleTestRunner(
        StudentInputParser parser,
        QuestionPrinter printer,
        Informer informer
    ) {
        this.parser = parser;
        this.printer = printer;
        this.informer = informer;
    }

    public Optional<StudentTest> run(Test test) {
        informer.inform(
            new StringBuilder("Starting ")
                .append(test.getName())
                .append(" test. Please enter your first name and last name (whitespace separated)")
                .toString()
        );

        return
            parser
                .parseToStudent()
                .map(student -> new StudentTest(student, test, ask(test)))
            ;
    }

    private List<AnsweredQuestion> ask(Test test) {
        informer.inform("Please, choose one answer or multiple (comma separated) ones:");

        return
            test
                .getQuestions()
                .stream()
                .map(
                    question -> {
                        printer.print(question);

                        return new AnsweredQuestion(question, parser.parseAnswers(question.getPossibleAnswers()));
                    }
                )
                .collect(Collectors.toList());
    }
}
