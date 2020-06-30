package ru.otus.spring.spring_shell_testing_app.service.shell.question;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.domain.student.Student;
import ru.otus.spring.spring_shell_testing_app.service.formatter.QuestionFormatter;
import ru.otus.spring.spring_shell_testing_app.service.provider.messages.MessagesProvider;
import ru.otus.spring.spring_shell_testing_app.service.shell.session.StudentTestSession;

@Service
public class ShellQuestionService implements QuestionService {
    private final static String ERROR_MESSAGE = "test.errorMessage";

    private final StudentTestSession studentSession;
    private final QuestionFormatter formatter;
    private final MessagesProvider messagesProvider;

    public ShellQuestionService(
        StudentTestSession studentSession,
        QuestionFormatter formatter,
        MessagesProvider messagesProvider
    ) {
        this.studentSession = studentSession;
        this.formatter = formatter;
        this.messagesProvider = messagesProvider;
    }

    @Override
    public String run() {
        return
            studentSession
                .currentQuestion()
                .map(formatter::format)
                .orElse(messagesProvider.provide(ERROR_MESSAGE))
        ;
    }
}
