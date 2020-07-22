package ru.otus.spring.spring_data_jpa_book_info_app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.service.author.AuthorService;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис работы с авторами должен ")
@Transactional
@SpringBootTest
public class AuthorServiceTest {
    private static final Author INITIAL_AUTHOR = new Author(1, "Some", "Author");
    private static final String UPDATED_FIRST_NAME = "Another";
    private static final String UPDATED_LAST_NAME = "Writer";

    @Autowired
    private AuthorService service;

    @DisplayName("обновлять данные автора, если он найден")
    @Test
    public void editStored() {
        var result = service.update(new Author(INITIAL_AUTHOR.getId(), UPDATED_FIRST_NAME, UPDATED_LAST_NAME));

        assertThat(result.isOk()).isTrue();

        var author = result.value().get();

        assertThat(author.getId()).isEqualTo(INITIAL_AUTHOR.getId());
        assertThat(author.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(author.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @DisplayName("отдавать пустой результат при попытке обновить данные несохранненого автора")
    @Test
    public void editNonStored() {
        var result = service.update(new Author(0, UPDATED_FIRST_NAME, UPDATED_LAST_NAME));


        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    @DisplayName("удалять автора, если он сохранен")
    @Test
    public void removeStored() {
        var result = service.remove(INITIAL_AUTHOR.getId());

        assertThat(result.isOk()).isTrue();
    }

    @DisplayName("отдавать пустой результат при попытке удалить несохраненного автора")
    @Test
    public void removeNonStored() {
        var result = service.remove(9);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }
}
