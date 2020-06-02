package ru.otus.spring.students_test_app;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.students_test_app.service.parser.TestFileParser;
import ru.otus.spring.students_test_app.service.printer.TestPrinter;

public class Main {

    private final static String CONTEXT_FILE = "/spring-context.xml";

    public static void main(String[] args) {
        var context = new ClassPathXmlApplicationContext(CONTEXT_FILE);

        var parser = context.getBean(TestFileParser.class);
        var printer = context.getBean(TestPrinter.class);

        parser
            .parse()
            .ifPresentOrElse(
                printer::print,
                () -> System.out.println("Some error occurred...")
            );
    }
}
