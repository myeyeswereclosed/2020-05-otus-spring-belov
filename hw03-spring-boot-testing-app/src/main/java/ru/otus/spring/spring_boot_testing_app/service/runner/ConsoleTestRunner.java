package ru.otus.spring.spring_boot_testing_app.service.runner;

import org.springframework.stereotype.Controller;
import ru.otus.spring.spring_boot_testing_app.domain.StudentTest;
import ru.otus.spring.spring_boot_testing_app.domain.Test;
import ru.otus.spring.spring_boot_testing_app.domain.question.AnsweredQuestion;
import ru.otus.spring.spring_boot_testing_app.service.input.StudentInputParser;
import ru.otus.spring.spring_boot_testing_app.service.informer.Informer;
import ru.otus.spring.spring_boot_testing_app.service.formatter.QuestionFormatter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ConsoleTestRunner implements TestRunner {
    private final StudentInputParser parser;
    private final QuestionFormatter formatter;
    private final Informer informer;

    public ConsoleTestRunner(
        StudentInputParser parser,
        QuestionFormatter printer,
        Informer informer
    ) {
        this.parser = parser;
        this.formatter = printer;
        this.informer = informer;
    }

    public Optional<StudentTest> run(Test test) {
        informer.startMessage();

        return
            parser
                .parseToStudent()
                .map(student -> new StudentTest(student, test, ask(test)))
            ;
    }

    private List<AnsweredQuestion> ask(Test test) {
        informer.chooseAnswer();

        return
            test
                .getQuestions()
                .stream()
                .map(
                    question -> {
                        informer.inform(formatter.format(question));

                        return new AnsweredQuestion(question, parser.parseAnswers(question.getPossibleAnswers()));
                    }
                )
                .collect(Collectors.toList());
    }
}
