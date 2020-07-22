package ru.otus.spring.spring_data_jpa_book_info_app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.spring_data_jpa_book_info_app.service.genre.GenreService;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис работы с жанрами должен ")
@Transactional
@SpringBootTest
public class GenreServiceTest {
    private static final Genre INITIAL_GENRE = new Genre(1, "horror");
    private static final String UPDATED_NAME = "drama";

    @Autowired
    private GenreService service;

    @DisplayName("обновлять название жанра, если он найден")
    @Test
    public void editStored() {
        var result = service.update(new Genre(INITIAL_GENRE.getId(), UPDATED_NAME));

        assertThat(result.isOk()).isTrue();

        var genre = result.value().get();

        assertThat(genre.getId()).isEqualTo(INITIAL_GENRE.getId());
        assertThat(genre.getName()).isEqualTo(UPDATED_NAME);
    }

    @DisplayName("отдавать пустой результат при попытке обновить данные несохранненого жанра")
    @Test
    public void editNonStored() {
        var result = service.update(new Genre(9, UPDATED_NAME));

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    @DisplayName("удалять жанр, если он сохранен")
    @Test
    public void removeStored() {
        var result = service.remove(INITIAL_GENRE.getId());

        assertThat(result.isOk()).isTrue();
    }

    @DisplayName("отдавать пустой результат при попытке удалить несохраненный жанр")
    @Test
    public void removeNonStored() {
        var result = service.remove(9);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }
}
