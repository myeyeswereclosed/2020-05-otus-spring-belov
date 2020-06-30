package ru.otus.spring.spring_shell_testing_app.service.shell.command_checker;

import org.springframework.shell.Availability;

public interface CommandChecker {
    Availability isLoginAllowed();

    Availability isTestStartAllowed();

    Availability isTestPassingAllowed();
}
