package ru.otus.spring.spring_boot_testing_app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring.spring_boot_testing_app.domain.StudentTest;
import ru.otus.spring.spring_boot_testing_app.domain.TestPassed;
import ru.otus.spring.spring_boot_testing_app.domain.TestResult;
import ru.otus.spring.spring_boot_testing_app.domain.student.Student;
import ru.otus.spring.spring_boot_testing_app.service.TestSystem;
import ru.otus.spring.spring_boot_testing_app.service.analyzer.TestAnalyzer;
import ru.otus.spring.spring_boot_testing_app.service.formatter.TestResultFormatter;
import ru.otus.spring.spring_boot_testing_app.service.informer.Informer;
import ru.otus.spring.spring_boot_testing_app.service.parser.TestSourceParser;
import ru.otus.spring.spring_boot_testing_app.service.runner.TestRunner;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSystemTest {
    private final static ru.otus.spring.spring_boot_testing_app.domain.Test TEST =
        new ru.otus.spring.spring_boot_testing_app.domain.Test(
            "Mock test",
            emptyList()
        );

    private final static StudentTest STUDENT_TEST =
        new StudentTest(
            new Student("John", "Doe"),
            TEST,
            emptyList()
        );

    private final static TestResult TEST_RESULT = new TestPassed(STUDENT_TEST, 4);
    private final static String FORMATTED_OUTPUT_STUB = "Just a stub";

    @Autowired
    private TestSystem testSystem;

    @MockBean
    private TestSourceParser parser;

    @MockBean
    private TestRunner testRunner;

    @MockBean
    private TestAnalyzer analyzer;

    @MockBean
    private TestResultFormatter formatter;

    @MockBean
    private Informer informer;

    @Test
    public void run() {
        when(parser.parse()).thenReturn(Optional.of(TEST));
        when(testRunner.run(TEST)).thenReturn(Optional.of(STUDENT_TEST));
        when(analyzer.analyze(STUDENT_TEST)).thenReturn(TEST_RESULT);
        when(formatter.format(TEST_RESULT)).thenReturn(FORMATTED_OUTPUT_STUB);

        testSystem.run();

        verify(testRunner).run(TEST);
        verify(analyzer).analyze(STUDENT_TEST);
        verify(formatter).format(TEST_RESULT);
        verify(informer).inform(FORMATTED_OUTPUT_STUB);
    }
}
