package ru.otus.spring.spring_shell_testing_app.service.shell;

import ru.otus.spring.spring_shell_testing_app.domain.student.Student;

public interface TestRunner {
    String login(Student student);

    String startTest();

    String showQuestion();

    String handleAnswer(String answer);
}
