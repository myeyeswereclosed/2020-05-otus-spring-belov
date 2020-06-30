package ru.otus.spring.spring_shell_testing_app.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.spring_shell_testing_app.config.Config;
import ru.otus.spring.spring_shell_testing_app.domain.StudentTest;
import ru.otus.spring.spring_shell_testing_app.domain.TestPassed;
import ru.otus.spring.spring_shell_testing_app.domain.TestResult;
import ru.otus.spring.spring_shell_testing_app.domain.answer.CorrectAnswer;
import ru.otus.spring.spring_shell_testing_app.domain.answer.IncorrectAnswer;
import ru.otus.spring.spring_shell_testing_app.domain.question.AnsweredQuestion;
import ru.otus.spring.spring_shell_testing_app.domain.question.Question;
import ru.otus.spring.spring_shell_testing_app.domain.student.Student;
import ru.otus.spring.spring_shell_testing_app.service.formatter.QuestionFormatter;
import ru.otus.spring.spring_shell_testing_app.service.formatter.TestResultFormatter;
import ru.otus.spring.spring_shell_testing_app.service.input.ShellStudentAnswerParser;
import ru.otus.spring.spring_shell_testing_app.service.parser.TestSourceParser;
import ru.otus.spring.spring_shell_testing_app.service.shell.login.LoginService;
import ru.otus.spring.spring_shell_testing_app.service.shell.session.StudentTestSession;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Shell система тестирования ")
@SpringBootTest
public class ShellTestSystemTest {
    // login
    private static List<String> CORRECT_LOGIN_COMMANDS = Arrays.asList("login Ivan Petrov", "l Ivan Petrov");
    private static List<String> INCORRECT_LOGIN_COMMANDS =
        Arrays.asList("login", "l", "login a b", "l ab c", "l a bc");

    private final static String LOGIN_SERVICE_MESSAGE = "Response from login service";

    // start
    private static List<String> CORRECT_START_COMMANDS = Arrays.asList("start", "s");

    // answer
    private static List<String> CORRECT_ANSWER_COMMANDS = Arrays.asList("answer 1", "a 1,3");

    private final static Question FIRST_QUESTION =
        new Question(
            "2x2 = ", Map.of(1, new CorrectAnswer("4"), 2, new IncorrectAnswer("5"))
        );

    private final static Question SECOND_QUESTION =
        new Question(
            "2x3 = ", Map.of(1, new IncorrectAnswer("4"), 2, new CorrectAnswer("6"))
        );

    private final static ru.otus.spring.spring_shell_testing_app.domain.Test TEST =
        new ru.otus.spring.spring_shell_testing_app.domain.Test(
            "Mock test",
            Arrays.asList(FIRST_QUESTION, SECOND_QUESTION)
        );

    private final static StudentTest STUDENT_TEST =
        new StudentTest(
            new Student("John", "Doe"),
            TEST,
            Arrays.asList(
                new AnsweredQuestion(FIRST_QUESTION, Collections.singletonList(new CorrectAnswer("4"))),
                new AnsweredQuestion(SECOND_QUESTION, Collections.singletonList(new CorrectAnswer("6")))
            )
        );

    private final static TestResult RESULT = new TestPassed(STUDENT_TEST,2);

    @MockBean
    private LoginService loginService;

    @MockBean
    private StudentTestSession sessionMock;

    @MockBean
    private TestSourceParser parserMock;

    @MockBean
    private Config config;

    @MockBean
    private ShellStudentAnswerParser parser;

    @Autowired
    private Shell shell;

    @Autowired
    private QuestionFormatter questionFormatter;

    @Autowired
    private TestResultFormatter resultFormatter;

    @DisplayName(" должна возвращать приветствие для корректных форм команды логина")
    @Test
    void shouldReturnExpectedGreetingAfterLoginCommandEvaluated() {
        when(loginService.run(any(Student.class))).thenReturn(LOGIN_SERVICE_MESSAGE);

        CORRECT_LOGIN_COMMANDS.forEach(
            loginCommand -> {
                String res = (String) shell.evaluate(() -> loginCommand);

                assertThat(res).isEqualTo(LOGIN_SERVICE_MESSAGE);
            }
        );
    }

    @DisplayName(" не должна обработать некорректные формы команды логина")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldThrowExceptionOnIncorrectLoginCommand() {
        INCORRECT_LOGIN_COMMANDS.forEach(
            loginCommand -> {
                try {
                    var res = (String) shell.evaluate(() -> loginCommand);

                    fail("Incorrect login command not rejected");
                } catch (Exception e) {

                }
            }
        );
    }

    @DisplayName(" должна стартовать тест, если пользователь залогинен")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldStartTestForLoggedInUser() {
        when(sessionMock.isStarted()).thenReturn(true);
        when(sessionMock.currentQuestion()).thenReturn(Optional.of(FIRST_QUESTION));
        when(parserMock.parse()).thenReturn(Optional.of(TEST));

        CORRECT_START_COMMANDS.forEach(
            startCommand -> {
                var res = (String) shell.evaluate(() -> startCommand);

                assertThat(res).isEqualTo(questionFormatter.format(FIRST_QUESTION));
            }
        );
    }

    @DisplayName(" не должна стартовать тест, если пользователь не залогинен")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldNotStartTestForLoggedInUser() {
        when(sessionMock.isStarted()).thenReturn(false);

        CORRECT_START_COMMANDS.forEach(
            startCommand ->
                assertThat(shell.evaluate(() -> startCommand)).isInstanceOf(CommandNotCurrentlyAvailable.class)
        );
    }

    @DisplayName(" должна принимать ответы если пользователь залогинился и начал тестирование")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldAcceptAnswerIfTestStarted() {
        when(config.getAnswersDelimiter()).thenReturn(",");
        when(sessionMock.isStarted()).thenReturn(true);
        when(sessionMock.isTestInProgress()).thenReturn(true);
        when(sessionMock.currentQuestion())
            .thenReturn(Optional.of(FIRST_QUESTION))
            .thenReturn(Optional.of(SECOND_QUESTION));
        when(sessionMock.addAnsweredQuestion(any(AnsweredQuestion.class))).thenReturn(sessionMock);
        when(sessionMock.studentTest()).thenReturn(Optional.empty());

        CORRECT_ANSWER_COMMANDS.forEach(
            answerCommand -> {
                var res = (String) shell.evaluate(() -> answerCommand);

                assertThat(res).isEqualTo(questionFormatter.format(SECOND_QUESTION));
            }
        );
    }

    @DisplayName(" должна вывести результат после ответа на последний вопрос")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldPerformResultIfAllQuestionsAreAnswered() {
        when(config.getAnswersDelimiter()).thenReturn(",");
        when(sessionMock.isStarted()).thenReturn(true);
        when(sessionMock.isTestInProgress()).thenReturn(true);
        when(sessionMock.currentQuestion())
            .thenReturn(Optional.of(FIRST_QUESTION))
            .thenReturn(Optional.of(SECOND_QUESTION));
        when(sessionMock.addAnsweredQuestion(any(AnsweredQuestion.class))).thenReturn(sessionMock);
        when(sessionMock.studentTest()).thenReturn(Optional.of(STUDENT_TEST));

        CORRECT_ANSWER_COMMANDS.forEach(
            answerCommand -> {
                var res = (String) shell.evaluate(() -> answerCommand);

                assertThat(res).isEqualTo(resultFormatter.format(RESULT));
            }
        );
    }

    @DisplayName(" не должна принимать ответы если пользователь не залогинился")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void shouldNotAcceptAnswerIfUserNotLoggedIn() {
        when(sessionMock.isStarted()).thenReturn(false);

        CORRECT_ANSWER_COMMANDS.forEach(
            command ->
                assertThat(shell.evaluate(() -> command)).isInstanceOf(CommandNotCurrentlyAvailable.class)
        );
    }

    @DisplayName(" не должна принимать ответы если пользователь не начал тест")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void shouldNotAcceptAnswerIfTestNotStarted() {
        when(sessionMock.isTestInProgress()).thenReturn(false);

        CORRECT_ANSWER_COMMANDS.forEach(
            command ->
                assertThat(shell.evaluate(() -> command)).isInstanceOf(CommandNotCurrentlyAvailable.class)
        );
    }
}

