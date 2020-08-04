package ru.otus.spring.web_ui_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;
import ru.otus.spring.web_ui_book_info_app.repository.genre.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@ComponentScan(
    {
        "ru.otus.spring.web_ui_book_info_app.migration",
        "ru.otus.spring.web_ui_book_info_app.repository",
        "ru.otus.spring.web_ui_book_info_app.config"
    }
)
@DisplayName("Репозиторий жанров должен ")
@DataMongoTest
public class GenreRepositoryTest {
    private static final Genre INITIAL_GENRE = new Genre("horror");
    private static final Genre UPDATED_GENRE = new Genre("love-story");

    private static final String INITIAL_GENRE_NOT_FOUND = "Initial genre not found";

    @Autowired
    private GenreRepository repository;

    @DisplayName("обновлять название жанра")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void update() {
        initialGenre()
            .ifPresentOrElse(
                this::testGenreUpdated,
                () -> fail(INITIAL_GENRE_NOT_FOUND)
            );
    }

    private void testGenreUpdated(Genre initialGenre) {
        var maybeGenre = repository.update(new Genre(initialGenre.getId(), UPDATED_GENRE.getName()));

        assertThat(maybeGenre).get().isEqualTo(initialGenre);
        assertThat(initialGenre()).isEmpty();
        assertThat(repository.findByName(UPDATED_GENRE.getName())).isPresent();
    }

    @DisplayName("отдавать пустой результат при попытке обновить несохраненный жанр")
    @Test
    public void updateNonStored() {
        assertThat(repository.update(new Genre("NonStoredId", UPDATED_GENRE.getName()))).isEmpty();
    }

    @DisplayName("удалять жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void delete() {
        initialGenre()
            .ifPresentOrElse(
                genre -> {
                    assertThat(repository.delete(genre.getId())).get().isEqualTo(genre.getId());
                    assertThat(repository.findAll()).isEmpty();
                },
                () -> fail(INITIAL_GENRE_NOT_FOUND)
            );
    }

    @DisplayName("не удалять жанр ро несуществующему идентификатору")
    @Test
    public void deleteNonStored() {
        assertThat(repository.delete("NonStoredId")).isEmpty();
        assertThat(initialGenre()).isPresent();
    }

    private Optional<Genre> initialGenre() {
        assertThat(repository.findAll().size()).isEqualTo(1);

        return repository.findByName(INITIAL_GENRE.getName());
    }
}
