package ru.otus.spring.spring_shell_testing_app.service;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import ru.otus.spring.spring_shell_testing_app.domain.student.Student;
import ru.otus.spring.spring_shell_testing_app.service.shell.command_checker.CommandChecker;
import ru.otus.spring.spring_shell_testing_app.service.shell.ShellTestRunner;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static org.springframework.util.StringUtils.capitalize;

@ShellComponent
public class ShellTestSystem {
    private final ShellTestRunner testRunner;
    private final CommandChecker commandChecker;

    public ShellTestSystem(ShellTestRunner testRunner, CommandChecker commandChecker) {
        this.testRunner = testRunner;
        this.commandChecker = commandChecker;
    }

    @ShellMethod(value = "Login command", key = {"l", "login"})
    @ShellMethodAvailability(value = "loginIsAllowed")
    public String login(
        @NotEmpty @Size(min = 2, max=20) String firstName,
        @NotEmpty @Size(min = 2, max=30) String lastName
    ) {
        return testRunner.login(new Student(capitalize(firstName), capitalize(lastName)));
    }

    @ShellMethod(value = "Start command", key = {"s", "start"})
    @ShellMethodAvailability(value = "testStartIsAllowed")
    public String start() {
        return testRunner.startTest();
    }

    @ShellMethod(value = "Answer command", key = {"a", "answer"})
    @ShellMethodAvailability(value = "testPassingIsAllowed")
    public String answer(String input) {
        return testRunner.handleAnswer(input);
    }

    @ShellMethod(value = "Question command", key = {"q", "question"})
    @ShellMethodAvailability(value = "testPassingIsAllowed")
    public String question() {
        return testRunner.showQuestion();
    }

    private Availability loginIsAllowed() {
        return commandChecker.isLoginAllowed();
    }

    private Availability testStartIsAllowed() {
        return commandChecker.isTestStartAllowed();
    }

    private Availability testPassingIsAllowed() {
        return commandChecker.isTestPassingAllowed();
    }
}