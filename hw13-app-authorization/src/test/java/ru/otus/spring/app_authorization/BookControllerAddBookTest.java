package ru.otus.spring.app_authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.otus.spring.app_authorization.controller.book.BookController;

import java.util.Arrays;

@DisplayName("Контроллер книг должен ")
@WebMvcTest(BookController.class)
@RunWith(Parameterized.class)
public class BookControllerAddBookTest {
    @Parameterized.Parameters
    public static  Iterable<Object[]> data() {
        return
            Arrays.asList(
                new Object[][] {

                }
            );
    }
}
