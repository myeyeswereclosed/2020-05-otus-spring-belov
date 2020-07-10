package ru.otus.spring.book_info_app.service.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.QueryTimeoutException;
import ru.otus.spring.book_info_app.dao.genre.GenreDao;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@DisplayName("Сервис по работе c жанрами должен ")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GenreServiceTest {
    private final static Genre GENRE = new Genre(0, "horror");
    private final static Genre GENRE_STORED = new Genre(20, "horror");

    @MockBean
    private GenreDao dao;

    @Autowired
    GenreServiceImpl service;

    @DisplayName("вернуть успешный результат, если жанр был сохранен")
    @Test
    public void create() {
        when(dao.save(GENRE)).thenReturn(GENRE_STORED);

        var result = service.create(GENRE);

        assertSuccessfulResult(result);
    }

    @DisplayName("вернуть ошибку, если возникает исключение")
    @Test
    public void handleException() {
        when(dao.save(GENRE)).thenThrow(new QueryTimeoutException("Timeout1"));
        doAnswer(i -> {
            throw new QueryTimeoutException("Timeout2");
        }).when(dao).update(GENRE);
        doAnswer(i -> {
            throw new QueryTimeoutException("Timeout3");
        }).when(dao).delete(GENRE.getId());

        Arrays.asList(
            service.create(GENRE),
            service.update(GENRE),
            service.remove(GENRE.getId())
        ).forEach(
            result -> assertThat(result.isOk()).isFalse()
        );
    }

    @Test
    @DisplayName("вернуть успешный результат, если жанр был обновлен")
    public void renameBook() {
        doAnswer(i -> {
            return null;
        }).when(dao).update(GENRE);

        var result = service.update(GENRE);

        assertThat(result.isOk());
    }

    @Test
    @DisplayName("вернуть успешный пустой результат, если жанр была удален")
    public void removeBook() {
        doAnswer(i -> {
            return null;
        }).when(dao).delete(GENRE.getId());

        var result = service.remove(GENRE.getId());

        assertThat(result.isOk());
        assertThat(result.value().isEmpty());
    }

    private void assertSuccessfulResult(ServiceResult<Genre> result) {
        assertThat(result.isOk());
        assertThat(result.value().get()).isEqualTo(GENRE_STORED);
    }
}
