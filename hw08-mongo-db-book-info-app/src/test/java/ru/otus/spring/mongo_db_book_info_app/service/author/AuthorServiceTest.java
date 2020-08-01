package ru.otus.spring.mongo_db_book_info_app.service.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис авторов должен ")
@SpringBootTest
public class AuthorServiceTest {
    private static final Author INITIAL_AUTHOR = new Author("Some", "Author");
    private static final Author AUTHOR = new Author("Oleg", "Enotov");

    @Autowired
    private AuthorService service;

    @DisplayName("сохранять нового автора")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void create() {
        var result = service.create(AUTHOR);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).get().extracting(Author::getId).isNotNull();
        assertThat(result.value()).get().satisfies(
            author -> assertThat(author.hasFirstAndLastName(AUTHOR.getFirstName(), AUTHOR.getLastName())).isTrue()
        );
    }

    @DisplayName("возвращать ошибку при сохранении уже существующего автора")
    @Test
    public void createAlreadyStored() {
        var result = service.create(INITIAL_AUTHOR);

        assertThat(result.isOk()).isFalse();
    }
}
