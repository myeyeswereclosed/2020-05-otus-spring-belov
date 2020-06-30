package ru.otus.spring.spring_shell_testing_app.service.shell.starter;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.service.parser.TestSourceParser;
import ru.otus.spring.spring_shell_testing_app.service.provider.messages.MessagesProvider;
import ru.otus.spring.spring_shell_testing_app.service.shell.question.QuestionService;
import ru.otus.spring.spring_shell_testing_app.service.shell.session.StudentTestSession;

@Service
public class ShellTestStarter implements TestStarter {
    private final static String ERROR_MESSAGE = "test.errorMessage";

    private final StudentTestSession studentSession;
    private final TestSourceParser parser;
    private final QuestionService questionService;
    private final MessagesProvider messagesProvider;

    public ShellTestStarter(
        StudentTestSession studentSession,
        TestSourceParser parser,
        QuestionService questionService,
        MessagesProvider messagesProvider
    ) {
        this.studentSession = studentSession;
        this.parser = parser;
        this.questionService = questionService;
        this.messagesProvider = messagesProvider;
    }

    @Override
    public String run() {
        return
            studentSession.isTestInProgress()
                ? questionService.run()
                : parser
                    .parse()
                    .map(
                        test -> {
                            studentSession.startTest(test);

                            return questionService.run();
                        }
                    )
                    .orElse(messagesProvider.provide(ERROR_MESSAGE))
        ;
    }
}
