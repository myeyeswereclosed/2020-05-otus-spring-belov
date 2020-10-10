package ru.otus.spring.actuator.health;

import org.junit.jupiter.api.DisplayName;
import ru.otus.spring.actuator.domain.Author;

@DisplayName("Health check уникальности авторов должен")
public class AuthorUniquenessHealthCheckTest extends BaseUniquenessHealthCheckTest<Author> {
    private static final String INITIAL_AUTHOR_FIRST_NAME = "Some";
    private static final String INITIAL_AUTHOR_LAST_NAME = "Author";

    @Override
    protected String serviceUpContent() {
        return "\"authorUniqueness\":{\"status\":\"UP\",\"details\":{\"message\":\"No duplicates\"}}";
    }

    @Override
    protected String serviceDownContent() {
        return "\"authorUniqueness\":{\"status\":\"DOWN\",\"details\":{\"duplicates found are:\":[\"Some Author\"]}}";
    }

    @Override
    protected boolean checkItem(Author author) {
        return author.hasFirstAndLastName(INITIAL_AUTHOR_FIRST_NAME, INITIAL_AUTHOR_LAST_NAME);
    }

    @Override
    protected Author testItem() {
        return new Author(INITIAL_AUTHOR_FIRST_NAME, INITIAL_AUTHOR_LAST_NAME);
    }
}
