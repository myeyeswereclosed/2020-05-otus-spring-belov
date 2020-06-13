package ru.otus.spring.students_testing.service.parser;

import org.junit.Assert;
import ru.otus.spring.students_testing.domain.Test;
import ru.otus.spring.students_testing.service.reader.FileReader;

import java.util.*;

public class CsvTestFileParserTest {
    private final static List<String> CORRECT_TEST_FILE_CONTENTS =
        Arrays.asList(
            "2x2 = ... ,4,true,3,false,2,false,5,false",
            "SOLID includes,SRP,true,KISS,false,DRY,false,DIP,true"
        )
    ;

    private final static List<List<String>> INCORRECT_TEST_FILES_CONTENTS =
        Arrays.asList(
            Collections.emptyList(),
            Collections.singletonList(""),
            Collections.singletonList(null),
            Collections.singletonList("something strange"),
            Collections.singletonList("Question, Just one answer, true"),
            Collections.singletonList("Question, Incorrect number of questions, true, Answer1, true, Answer2")
        );

    @org.junit.Test
    public void successfulParsing() {
        var fileReader = new FileReaderMock(CORRECT_TEST_FILE_CONTENTS);

        var parser = new CsvTestFileParser(fileReader);

        var maybeTest = parser.parse();

        assertSuccessfulParsing(maybeTest.get());
    }

    @org.junit.Test
    public void parsingFailure() {
        INCORRECT_TEST_FILES_CONTENTS.forEach(
            contents -> {
                var fileReader = new FileReaderMock(contents);

                var parser = new CsvTestFileParser(fileReader);

                Assert.assertTrue(parser.parse().isEmpty());
            }
        );
    }

    private void assertSuccessfulParsing(Test test) {
        Assert.assertEquals(2, test.getQuestions().size());
        Assert.assertEquals("2x2 = ... ", test.getQuestions().get(0).getText());
        Assert.assertEquals("SOLID includes", test.getQuestions().get(1).getText());
    }

    private class FileReaderMock implements FileReader {
        private List<String> contents;

        public FileReaderMock(List<String> contents) {
            this.contents = contents;
        }

        @Override
        public List<String> read() {
            return contents;
        }
    }
}
