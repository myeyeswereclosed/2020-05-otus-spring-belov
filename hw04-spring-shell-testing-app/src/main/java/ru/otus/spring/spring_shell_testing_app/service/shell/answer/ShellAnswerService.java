package ru.otus.spring.spring_shell_testing_app.service.shell.answer;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.domain.question.AnsweredQuestion;
import ru.otus.spring.spring_shell_testing_app.service.analyzer.TestAnalyzer;
import ru.otus.spring.spring_shell_testing_app.service.formatter.TestResultFormatter;
import ru.otus.spring.spring_shell_testing_app.service.input.StudentAnswerParser;
import ru.otus.spring.spring_shell_testing_app.service.provider.messages.MessagesProvider;
import ru.otus.spring.spring_shell_testing_app.service.shell.question.QuestionService;
import ru.otus.spring.spring_shell_testing_app.service.shell.session.StudentTestSession;

@Service
public class ShellAnswerService implements AnswerService {
    private final static String ERROR_MESSAGE = "test.errorMessage";

    private final StudentTestSession studentSession;
    private final StudentAnswerParser studentAnswerParser;
    private final MessagesProvider messagesProvider;
    private final TestAnalyzer analyzer;
    private final TestResultFormatter testResultFormatter;
    private final QuestionService questionService;

    public ShellAnswerService(
        StudentTestSession studentSession,
        StudentAnswerParser studentAnswerParser,
        MessagesProvider messagesProvider,
        TestAnalyzer analyzer,
        TestResultFormatter testResultFormatter,
        QuestionService questionService
    ) {
        this.studentSession = studentSession;
        this.studentAnswerParser = studentAnswerParser;
        this.messagesProvider = messagesProvider;
        this.analyzer = analyzer;
        this.testResultFormatter = testResultFormatter;
        this.questionService = questionService;
    }

    @Override
    public String run(String answer) {
        return
            studentSession
                .currentQuestion()
                .map(
                    currentQuestion -> {
                        var chosenAnswers =
                            studentAnswerParser.parseAnswers(currentQuestion.getPossibleAnswers(), answer);

                        return
                            studentSession
                                .addAnsweredQuestion(new AnsweredQuestion(currentQuestion, chosenAnswers))
                                .studentTest()
                                .map(testFinished -> testResultFormatter.format(analyzer.analyze(testFinished)))
                                .orElseGet(questionService::run)
                            ;
                    }
                )
                .orElse(messagesProvider.provide(ERROR_MESSAGE))
            ;
    }
}
