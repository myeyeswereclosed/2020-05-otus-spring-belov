package ru.otus.spring.spring_boot_testing_app.runner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring.spring_boot_testing_app.domain.StudentTest;
import ru.otus.spring.spring_boot_testing_app.domain.answer.CorrectAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.answer.IncorrectAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.answer.PossibleAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.question.Question;
import ru.otus.spring.spring_boot_testing_app.domain.student.Student;
import ru.otus.spring.spring_boot_testing_app.service.informer.Informer;
import ru.otus.spring.spring_boot_testing_app.service.input.StudentInputParser;
import ru.otus.spring.spring_boot_testing_app.service.runner.ConsoleTestRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRunnerTest {
    private static final PossibleAnswer FIRST_QUESTION_RIGHT_ANSWER = new CorrectAnswer("4");
    private static final PossibleAnswer SECOND_QUESTION_WRONG_ANSWER = new IncorrectAnswer("4");

    private final static Map<Integer, PossibleAnswer> FIRST_QUESTION_ANSWERS =
        Map.of(
            1, new IncorrectAnswer("1"),
            2, new IncorrectAnswer("2"),
            3, new IncorrectAnswer("3"),
            4, FIRST_QUESTION_RIGHT_ANSWER
        );

    private final static Map<Integer, PossibleAnswer> SECOND_QUESTION_ANSWERS =
        Map.of(
            1, new IncorrectAnswer("6"),
            2, new IncorrectAnswer("5"),
            3, SECOND_QUESTION_WRONG_ANSWER,
            4, new CorrectAnswer("3")
        );

    private final static ru.otus.spring.spring_boot_testing_app.domain.Test TEST =
        new ru.otus.spring.spring_boot_testing_app.domain.Test(
            "Mock test",
            Arrays.asList(
                new Question("2x2 = ...", FIRST_QUESTION_ANSWERS),
                new Question("2x3 = ...", SECOND_QUESTION_ANSWERS)
            )
        );

    @Autowired
    private ConsoleTestRunner runner;

    @MockBean
    private Informer informerMock;

    @MockBean
    private StudentInputParser parserMock;

    @Test
    public void run() {
        Mockito
            .when(parserMock.parseToStudent())
            .thenReturn(Optional.of(new Student("John", "Doe")));

        Mockito
            .when(parserMock.parseAnswers(FIRST_QUESTION_ANSWERS))
            .thenReturn(singletonList(FIRST_QUESTION_RIGHT_ANSWER))
        ;

        Mockito
            .when(parserMock.parseAnswers(SECOND_QUESTION_ANSWERS))
            .thenReturn(singletonList(SECOND_QUESTION_WRONG_ANSWER))
        ;

        var result = runner.run(TEST);

        assertStudentTest(result.get());
    }

    private void assertStudentTest(StudentTest studentTest) {
        var student = studentTest.getStudent();

        Assert.assertTrue(student.hasFirstName("John"));
        Assert.assertTrue(student.hasLastName("Doe"));
        Assert.assertEquals(TEST, studentTest.getTest());

        var answeredQuestions = studentTest.getAnsweredQuestions();

        Assert.assertEquals(2, answeredQuestions.size());

        var firstAnsweredQuestion = answeredQuestions.get(0);
        var secondAnsweredQuestion = answeredQuestions.get(1);

        assertChosenAnswers(FIRST_QUESTION_RIGHT_ANSWER, firstAnsweredQuestion.getChosenAnswers());
        assertChosenAnswers(SECOND_QUESTION_WRONG_ANSWER, secondAnsweredQuestion.getChosenAnswers());
    }

    private void assertChosenAnswers(PossibleAnswer expectedAnswer, List<PossibleAnswer> chosenAnswers) {
        Assert.assertEquals(1, chosenAnswers.size());
        Assert.assertEquals(expectedAnswer, chosenAnswers.get(0));
    }

}
