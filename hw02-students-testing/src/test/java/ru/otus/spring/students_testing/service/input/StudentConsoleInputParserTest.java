package ru.otus.spring.students_testing.service.input;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.spring.students_testing.domain.answer.CorrectAnswer;
import ru.otus.spring.students_testing.domain.answer.IncorrectAnswer;
import ru.otus.spring.students_testing.domain.answer.PossibleAnswer;
import ru.otus.spring.students_testing.domain.question.Question;
import ru.otus.spring.students_testing.domain.student.Student;

import java.util.*;

import static java.util.Map.entry;

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
            entry("1", Collections.singletonList(new CorrectAnswer("PHP"))),
            entry("4", Collections.singletonList(new CorrectAnswer("Perl"))),
            entry("1, 4", Arrays.asList(new CorrectAnswer("PHP"), new CorrectAnswer("Perl"))),
            entry("1,2", Arrays.asList(new CorrectAnswer("PHP"), new IncorrectAnswer("Java"))),
            entry("1, 2 ", Arrays.asList(new CorrectAnswer("PHP"), new IncorrectAnswer("Java"))),
            entry("0", Collections.emptyList()),
            entry("5", Collections.emptyList()),
            entry("0, 5 ", Collections.emptyList()),
            entry("0, 1", Collections.singletonList(new CorrectAnswer("PHP"))),
            entry("0, 2", Collections.singletonList(new IncorrectAnswer("Java"))),
            entry(" ", Collections.emptyList()),
            entry("", Collections.emptyList()),
            entry("1, 1, 1, 1, 1, 1", Collections.singletonList(new CorrectAnswer("PHP"))),
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

    @Test
    public void parseCorrectStudentDataInput() {
       var inputReader = new InputReaderMock(CORRECT_STUDENT_DATA);

       var parser = new StudentConsoleInputParser(inputReader);

       var student = parser.parseToStudent().get();

       assertCorrectDataParsedCorrectly(student);
    }

    @Test
    public void parseStudentAnswer() {
        CHOSEN_ANSWERS.forEach(
            (answer, expected) -> {
                var inputReader = new InputReaderMock(answer);

                var parser = new StudentConsoleInputParser(inputReader);

                var answersChosen = parser.parseAnswers(QUESTION.getPossibleAnswers());

                Assert.assertEquals(expected, answersChosen);
            }
        );
    }

    private void assertCorrectDataParsedCorrectly(Student student) {
        Assert.assertTrue(student.hasFirstName("John"));
        Assert.assertTrue(student.hasLastName("Doe"));
    }

    private static class InputReaderMock implements UserInputReader {
        private String result;

        InputReaderMock(String result) {
            this.result = result;
        }

        @Override
        public Optional<String> read() {
            return Optional.ofNullable(result);
        }
    }
}
