package ru.otus.spring.spring_shell_testing_app.service.shell.login;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.domain.student.Student;
import ru.otus.spring.spring_shell_testing_app.service.provider.messages.MessagesProvider;
import ru.otus.spring.spring_shell_testing_app.service.shell.session.StudentTestSession;

@Service
public class ShellLoginService implements LoginService {
    private final static String START_MESSAGE = "test.startMessage";

    private final StudentTestSession studentSession;
    private final MessagesProvider messagesProvider;

    public ShellLoginService(StudentTestSession studentSession, MessagesProvider messagesProvider) {
        this.studentSession = studentSession;
        this.messagesProvider = messagesProvider;
    }

    @Override
    public String run(Student student) {
        studentSession.start(student);

        return messagesProvider.provide(START_MESSAGE, new String[]{student.toString()});
    }
}
