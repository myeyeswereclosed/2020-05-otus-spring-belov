package ru.otus.spring.students_test_app.parser;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.spring.students_test_app.service.parser.CsvTestFileParser;

public class TestFileParserTest {

    private final static String FILE = "/example.csv";

    @Test
    public void successfulParsing() {
        var maybeTest = new CsvTestFileParser(FILE).parse();

        Assert.assertTrue(maybeTest.isPresent());

        var test = maybeTest.get();

        Assert.assertEquals("Students test", test.getName());
        Assert.assertEquals(1, test.getQuestions().size());

        var testQuestion = test.getQuestions().get(0);

        Assert.assertEquals("2x2 = ?", testQuestion.getText());

        var possibleAnswers = testQuestion.getPossibleAnswers();

        Assert.assertEquals(2, possibleAnswers.size());

        Assert.assertEquals("4", possibleAnswers.get(0).getText());
        Assert.assertTrue(possibleAnswers.get(0).isCorrect());

        Assert.assertEquals("3", possibleAnswers.get(1).getText());
        Assert.assertFalse(possibleAnswers.get(1).isCorrect());
    }
}
