package ru.otus.spring.book_info_app.service.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.QueryTimeoutException;
import ru.otus.spring.book_info_app.dao.author.AuthorDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@DisplayName("Сервис по работе c авторами должен ")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AuthorServiceTest {
    private final static Author AUTHOR = new Author(0, "Some", "Author");
    private final static Author AUTHOR_STORED = new Author(31, "Some", "Author");

    @MockBean
    private AuthorDao dao;

    @Autowired
    AuthorServiceImpl service;

    @DisplayName("вернуть успешный результат, если автор был сохранен")
    @Test
    public void create() {
        when(dao.save(AUTHOR)).thenReturn(AUTHOR_STORED);

        var result = service.create(AUTHOR);

        assertSuccessfulResult(result);
    }

    @DisplayName("вернуть ошибку, если возникает исключение")
    @Test
    public void handleException() {
        when(dao.save(AUTHOR)).thenThrow(new QueryTimeoutException("Timeout1"));
        doAnswer(i -> {
            throw new QueryTimeoutException("Timeout2");
        }).when(dao).update(AUTHOR);
        doAnswer(i -> {
            throw new QueryTimeoutException("Timeout3");
        }).when(dao).delete(AUTHOR.getId());

        Arrays.asList(
            service.create(AUTHOR),
            service.update(AUTHOR),
            service.remove(AUTHOR.getId())
        ).forEach(
            result -> assertThat(result.isOk()).isFalse()
        );
    }

    @Test
    @DisplayName("вернуть успешный результат, если книга была обновлена ")
    public void renameBook() {
        doAnswer(i -> {
            return null;
        }).when(dao).update(AUTHOR);

        var result = service.update(AUTHOR);

        assertThat(result.isOk());
    }

    @Test
    @DisplayName("вернуть успешный пустой результат, если книга была удалена ")
    public void removeBook() {
        doAnswer(i -> {
            return null;
        }).when(dao).delete(AUTHOR.getId());

        var result = service.remove(AUTHOR.getId());

        assertThat(result.isOk());
        assertThat(result.value().isEmpty());
    }

    private void assertSuccessfulResult(ServiceResult<Author> result) {
        assertThat(result.isOk());
        assertThat(result.value().get()).isEqualTo(AUTHOR_STORED);
    }
}
