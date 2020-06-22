package ru.otus.spring.spring_boot_testing_app.input;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.otus.spring.spring_boot_testing_app.domain.answer.CorrectAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.answer.IncorrectAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.answer.PossibleAnswer;
import ru.otus.spring.spring_boot_testing_app.domain.question.Question;
import ru.otus.spring.spring_boot_testing_app.domain.student.Student;
import ru.otus.spring.spring_boot_testing_app.service.input.StudentConsoleInputParser;
import ru.otus.spring.spring_boot_testing_app.service.input.UserInputReader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Map.entry;

@RunWith(MockitoJUnitRunner.class)
public class StudentConsoleInputParserTest {
    private final static String CORRECT_STUDENT_DATA = "John Doe";

    private final static Question QUESTION =
        new Question(
            "Scripting languages are ...",
            Map.of(
                1, new CorrectAnswer("PHP"),
                2, new IncorrectAnswer("Java"),
                3, new IncorrectAnswer("Esperanto"),
                4, new CorrectAnswer("Perl")
            )
        );

    private final static Map<String, List<PossibleAnswer>> CHOSEN_ANSWERS =
        Map.ofEntries(
            entry("1", singletonList(new CorrectAnswer("PHP"))),
            entry("4", singletonList(new CorrectAnswer("Perl"))),
            entry("1, 4", Arrays.asList(new CorrectAnswer("PHP"), new CorrectAnswer("Perl"))),
            entry("1,2", Arrays.asList(new CorrectAnswer("PHP"), new IncorrectAnswer("Java"))),
            entry("1, 2 ", Arrays.asList(new CorrectAnswer("PHP"), new IncorrectAnswer("Java"))),
            entry("0", emptyList()),
            entry("5", emptyList()),
            entry("0, 5 ", emptyList()),
            entry("0, 1", singletonList(new CorrectAnswer("PHP"))),
            entry("0, 2", singletonList(new IncorrectAnswer("Java"))),
            entry(" ", emptyList()),
            entry("", emptyList()),
            entry("1, 1, 1, 1, 1, 1", singletonList(new CorrectAnswer("PHP"))),
            entry("1, 1, 4, 4", Arrays.asList(new CorrectAnswer("PHP"), new CorrectAnswer("Perl"))),
            entry(
                "1, 2, 3, 4, 1, 1",
                Arrays.asList(
                    new CorrectAnswer("PHP"),
                    new IncorrectAnswer("Java"),
                    new IncorrectAnswer("Esperanto"),
                    new CorrectAnswer("Perl")
                )
            )
        );

    @Mock
    private UserInputReader inputReaderMock;

    @Test
    public void parseCorrectStudentDataInput() {
        var parser = new StudentConsoleInputParser(readerMock(CORRECT_STUDENT_DATA));

        var student = parser.parseToStudent().get();

        assertCorrectDataParsedCorrectly(student);
    }

    @Test
    public void parseStudentAnswer() {
        CHOSEN_ANSWERS.forEach(
            (answer, expected) -> {
                var parser = new StudentConsoleInputParser(readerMock(answer));

                var answersChosen = parser.parseAnswers(QUESTION.getPossibleAnswers());

                Assert.assertEquals(expected, answersChosen);
            }
        );
    }

    private void assertCorrectDataParsedCorrectly(Student student) {
        Assert.assertTrue(student.hasFirstName("John"));
        Assert.assertTrue(student.hasLastName("Doe"));
    }

    private UserInputReader readerMock(String valueToReturn) {
        Mockito.when(inputReaderMock.read()).thenReturn(Optional.of(valueToReturn));

        return inputReaderMock;
    }
}
