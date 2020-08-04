package ru.otus.spring.web_ui_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.repository.author.AuthorRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@ComponentScan(
    {
        "ru.otus.spring.web_ui_book_info_app.migration",
        "ru.otus.spring.web_ui_book_info_app.ru.otus.spring.web_ui_book_info_app.repository",
        "ru.otus.spring.web_ui_book_info_app.config"
    }
)
@DisplayName("Репозиторий авторов должен ")
@DataMongoTest
public class AuthorRepositoryTest {
    private static final Author INITIAL_AUTHOR = new Author("Some", "Author");
    private static final Author UPDATED_AUTHOR = new Author("Another", "One");

    private static final String INITIAL_AUTHOR_NOT_FOUND = "Initial author not found";

    @Autowired
    private AuthorRepository repository;

    @DisplayName("обновлять данные автора")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void update() {
        initialAuthor()
            .ifPresentOrElse(
                this::testAuthorUpdated,
                () -> fail(INITIAL_AUTHOR_NOT_FOUND)
            );
    }

    private void testAuthorUpdated(Author initialAuthor) {
        var maybeAuthor =
            repository.update(
                new Author(
                    initialAuthor.getId(),
                    UPDATED_AUTHOR.getFirstName(),
                    UPDATED_AUTHOR.getLastName())
            );

        assertThat(maybeAuthor).get().isEqualTo(initialAuthor);
        assertThat(initialAuthor()).isEmpty();
        assertThat(
            repository
                .findByFirstNameAndLastName(
                    UPDATED_AUTHOR.getFirstName(),
                    UPDATED_AUTHOR.getLastName())
        ).isPresent();
    }

    @DisplayName("отдавать пустой результат при попытке обновить несохраненного автора")
    @Test
    public void updateNonStored() {
        assertThat(
            repository.update(
                new Author(
                    "NonStoredId",
                    UPDATED_AUTHOR.getFirstName(),
                    UPDATED_AUTHOR.getLastName())
            )
        ).isEmpty();
    }

    @DisplayName("удалять автора")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void delete() {
        initialAuthor()
            .ifPresentOrElse(
                author -> {
                    assertThat(repository.delete(author.getId())).get().isEqualTo(author.getId());
                    assertThat(repository.findAll()).isEmpty();
                },
                () -> fail(INITIAL_AUTHOR_NOT_FOUND)
            );
    }

    @DisplayName("не удалять автора ро несуществующему идентификатору")
    @Test
    public void deleteNonStored() {
        assertThat(repository.delete("NonStoredId")).isEmpty();
        assertThat(initialAuthor()).isPresent();
    }

    private Optional<Author> initialAuthor() {
        assertThat(repository.findAll().size()).isEqualTo(1);

        return repository.findByFirstNameAndLastName(INITIAL_AUTHOR.getFirstName(), INITIAL_AUTHOR.getLastName());
    }
}
