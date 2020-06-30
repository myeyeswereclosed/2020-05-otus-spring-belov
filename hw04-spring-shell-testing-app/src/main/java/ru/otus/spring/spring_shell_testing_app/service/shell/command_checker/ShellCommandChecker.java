package ru.otus.spring.spring_shell_testing_app.service.shell.command_checker;

import org.springframework.shell.Availability;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.service.provider.messages.MessagesProvider;
import ru.otus.spring.spring_shell_testing_app.service.shell.session.StudentTestSession;

import static org.springframework.shell.Availability.unavailable;
import static org.springframework.shell.Availability.available;

@Service
public class ShellCommandChecker implements CommandChecker {
    private final static String LOGIN_MESSAGE = "test.loginMessage";
    private final static String ALREADY_LOGGED_IN_MESSAGE = "test.alreadyLoggedIn";
    private final static String TEST_NOT_STARTED = "test.startTestMessage";

    private final StudentTestSession session;
    private final MessagesProvider messagesProvider;

    public ShellCommandChecker(StudentTestSession session, MessagesProvider messagesProvider) {
        this.session = session;
        this.messagesProvider = messagesProvider;
    }

    @Override
    public Availability isLoginAllowed() {
        return
            session.isStarted()
                ? unavailable(
                    messagesProvider.provide(
                        ALREADY_LOGGED_IN_MESSAGE,
                        new String[]{session.getStudent().toString()})
                )
                : available()
        ;
    }

    @Override
    public Availability isTestStartAllowed() {
        return session.isStarted() ? available() : unavailable(messagesProvider.provide(LOGIN_MESSAGE));
    }

    @Override
    public Availability isTestPassingAllowed() {
        if (!session.isStarted()) {
            return unavailable(messagesProvider.provide(LOGIN_MESSAGE));
        }

        return session.isTestInProgress() ? available() : unavailable(messagesProvider.provide(TEST_NOT_STARTED));
    }
}
