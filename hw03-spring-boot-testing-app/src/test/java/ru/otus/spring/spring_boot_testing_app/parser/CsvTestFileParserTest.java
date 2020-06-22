package ru.otus.spring.spring_boot_testing_app.parser;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.otus.spring.spring_boot_testing_app.domain.Test;
import ru.otus.spring.spring_boot_testing_app.service.parser.CsvTestFileParser;
import ru.otus.spring.spring_boot_testing_app.service.reader.FileReader;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@RunWith(MockitoJUnitRunner.class)
public class CsvTestFileParserTest {
    private final static List<String> CORRECT_TEST_FILE_CONTENTS =
        Arrays.asList(
            "2x2 = ... ,4,true,3,false,2,false,5,false",
            "SOLID includes,SRP,true,KISS,false,DRY,false,DIP,true"
        )
    ;

    private final static List<List<String>> INCORRECT_TEST_FILE_CONTENTS =
        Arrays.asList(
            emptyList(),
            singletonList(""),
            singletonList(null),
            singletonList("something strange"),
            singletonList("Question, Just one answer, true"),
            singletonList("Question, Incorrect number of questions, true, Answer1, true, Answer2")
        );

    @Mock
    private FileReader fileReaderMock;

    @org.junit.Test
    public void successfulParsing() {
        var parser = new CsvTestFileParser(readerMock(CORRECT_TEST_FILE_CONTENTS));

        var maybeTest = parser.parse();

        assertSuccessfulParsing(maybeTest.get());
    }

    @org.junit.Test
    public void parsingFailure() {
        INCORRECT_TEST_FILE_CONTENTS.forEach(
            contents -> {
                var parser = new CsvTestFileParser(readerMock(contents));

                Assert.assertTrue(parser.parse().isEmpty());
            }
        );
    }

    private void assertSuccessfulParsing(Test test) {
        Assert.assertEquals(2, test.getQuestions().size());
        Assert.assertEquals("2x2 = ... ", test.getQuestions().get(0).getText());
        Assert.assertEquals("SOLID includes", test.getQuestions().get(1).getText());
    }

    private  FileReader readerMock(List<String> lines) {
        Mockito.when(fileReaderMock.read()).thenReturn(lines);

        return fileReaderMock;
    }
}
