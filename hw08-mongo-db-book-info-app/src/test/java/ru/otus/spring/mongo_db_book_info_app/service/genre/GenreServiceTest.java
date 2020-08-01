package ru.otus.spring.mongo_db_book_info_app.service.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис жанров должен ")
@SpringBootTest
public class GenreServiceTest {
    private static final Genre INITIAL_GENRE = new Genre("horror");
    private static final Genre NEW_GENRE  = new Genre("love-story");

    @Autowired
    private GenreService service;

    @DisplayName("сохранять новый жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void create() {
        var result = service.create(NEW_GENRE);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).get().extracting(Genre::getId).isNotNull();
        assertThat(result.value())
            .get()
            .satisfies(author -> assertThat(author.hasName(NEW_GENRE.getName())).isTrue());
    }

    @DisplayName("возвращать ошибку при сохранении уже существующего жанра")
    @Test
    public void createAlreadyStored() {
        var result = service.create(INITIAL_GENRE);

        assertThat(result.isOk()).isFalse();
    }
}
