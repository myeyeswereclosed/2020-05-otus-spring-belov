package ru.otus.spring.spring_boot_testing_app.service.formatter;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_boot_testing_app.domain.TestResult;
import ru.otus.spring.spring_boot_testing_app.domain.answer.PossibleAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.question.AnsweredQuestion;
import ru.otus.spring.spring_boot_testing_app.service.provider.messages.MessagesProvider;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleTestResultFormatter implements TestResultFormatter {
    private final static String RESULT_MESSAGE = "test.resultMessage";
    private final static String PASSED_MESSAGE = "test.success";
    private final static String FAILED_MESSAGE = "test.fail";
    private final static String NO_ANSWER = "test.noAnswerMessage";
    private final static String CORRECT_ANSWER = "test.correctAnswerMessage";
    private final static String INCORRECT_ANSWER = "test.incorrectAnswerMessage";

    private final MessagesProvider messagesProvider;

    public ConsoleTestResultFormatter(MessagesProvider messagesProvider) {
        this.messagesProvider = messagesProvider;
    }

    @Override
    public String format(TestResult result) {
        var resultOutput =
            result.isPassed()
                ? messagesProvider.provide(PASSED_MESSAGE)
                : messagesProvider.provide(FAILED_MESSAGE)
        ;

        var test = result.getTestMade();

        return
            messagesProvider.provide(
                RESULT_MESSAGE,
                new String[]{
                    test.getStudent().toString(),
                    String.valueOf(result.getPoints()),
                    String.valueOf(result.getTestMade().getTest().correctAnswersNumber()),
                    resultOutput,
                    test
                        .getAnsweredQuestions()
                        .stream()
                        .map(this::answeredQuestionOutput)
                        .collect(Collectors.joining("\r\n"))
                }
            );
    }

    private String answeredQuestionOutput(AnsweredQuestion answeredQuestion) {
        return answeredQuestion.getQuestion().getText() + " - " + answersOutput(answeredQuestion.getChosenAnswers());
    }

    private String answersOutput(List<PossibleAnswer> answers) {
        return
            answers.isEmpty()
                ? messagesProvider.provide(NO_ANSWER)
                : answers
                    .stream()
                    .map(answer -> answer.getText() + '(' + answerDescriptionOutput(answer) + ')')
                    .collect(Collectors.joining(", "))
            ;
    }

    private String answerDescriptionOutput(PossibleAnswer answer) {
        return
            answer.isCorrect()
                ? messagesProvider.provide(CORRECT_ANSWER)
                : messagesProvider.provide(INCORRECT_ANSWER)
        ;
    }
}
