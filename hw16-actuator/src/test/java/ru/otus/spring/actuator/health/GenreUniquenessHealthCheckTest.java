package ru.otus.spring.actuator.health;

import org.junit.jupiter.api.DisplayName;
import ru.otus.spring.actuator.domain.Genre;

@DisplayName("Health check уникальности жанров должен")
public class GenreUniquenessHealthCheckTest extends BaseUniquenessHealthCheckTest<Genre> {
    private static final String INITIAL_GENRE_NAME = "horror";

    @Override
    protected String serviceUpContent() {
        return "\"genreUniqueness\":{\"status\":\"UP\",\"details\":{\"message\":\"No duplicates\"}}";
    }

    @Override
    protected String serviceDownContent() {
        return "\"genreUniqueness\":{\"status\":\"DOWN\",\"details\":{\"duplicates found are:\":[\"horror\"]}}";
    }

    @Override
    protected boolean checkItem(Genre genre) {
        return genre.hasName(INITIAL_GENRE_NAME);
    }

    @Override
    protected Genre testItem() {
        return new Genre(INITIAL_GENRE_NAME);
    }
}
