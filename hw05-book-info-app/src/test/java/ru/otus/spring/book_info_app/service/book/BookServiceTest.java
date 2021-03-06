package ru.otus.spring.book_info_app.service.book;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.QueryTimeoutException;
import ru.otus.spring.book_info_app.dao.book.BookDao;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@DisplayName("Сервис по работе c книгами должен ")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class BookServiceTest {
    private final static Book BOOK = new Book(30, "Tri porosenka");
    private final static Book BOOK_STORED = new Book(30, "Tri porosenka");

    @MockBean
    private BookDao dao;

    @Autowired
    BookServiceImpl service;

    @DisplayName("вернуть успешный результат, если книга сохранена в бд")
    @Test
    public void addBook() {
        when(dao.save(BOOK)).thenReturn(BOOK_STORED);

        var result = service.addBook(BOOK);

        assertSuccessfulResult(result);
    }

    @DisplayName("вернуть ошибку, если возникает исключение")
    @Test
    public void handleException() {
        when(dao.save(BOOK)).thenThrow(new QueryTimeoutException("Timeout1"));
        doAnswer(i -> {
            throw new QueryTimeoutException("Timeout2");
        }).when(dao).update(BOOK);
        doAnswer(i -> {
            throw new QueryTimeoutException("Timeout3");
        }).when(dao).delete(BOOK.getId());

        Arrays.asList(
            service.addBook(BOOK),
            service.rename(BOOK),
            service.remove(BOOK.getId())
        ).forEach(
            result -> assertThat(result.isOk()).isFalse()
        );
    }

    @Test
    @DisplayName("вернуть успешный результат, если книга была обновлена")
    public void renameBook() {
        doAnswer(i -> {
            return null;
        }).when(dao).update(BOOK);

        var result = service.rename(BOOK);

        assertThat(result.isOk());
    }

    @Test
    @DisplayName("вернуть успешный пустой результат, если книга была удалена")
    public void removeBook() {
        doAnswer(i -> {
            return null;
        }).when(dao).delete(BOOK_STORED.getId());

        var result = service.remove(BOOK_STORED.getId());

        assertThat(result.isOk());
        assertThat(result.value().isEmpty());
    }

    private void assertSuccessfulResult(ServiceResult<Book> result) {
        assertThat(result.isOk());
        assertThat(result.value().get()).isEqualTo(BOOK_STORED);
    }
}
