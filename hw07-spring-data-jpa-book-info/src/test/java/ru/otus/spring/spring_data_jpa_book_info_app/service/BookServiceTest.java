package ru.otus.spring.spring_data_jpa_book_info_app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.spring_data_jpa_book_info_app.service.book.BookService;
import ru.otus.spring.spring_data_jpa_book_info_app.service.genre.GenreService;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис работы с книгами должен ")
@Transactional
@SpringBootTest
public class BookServiceTest {
    private static final Book INITIAL_BOOK = new Book(1, "Tri porosenka");
    private static final String UPDATED_TITLE = "Tri kotenka";

    @Autowired
    private BookService service;

    @DisplayName("обновлять название книги, если она найдена")
    @Test
    public void editStored() {
        var result = service.rename(new Book(INITIAL_BOOK.getId(), UPDATED_TITLE));

        assertThat(result.isOk()).isTrue();

        var book = result.value().get();

        assertThat(book.getId()).isEqualTo(INITIAL_BOOK.getId());
        assertThat(book.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @DisplayName("отдавать пустой результат при попытке обновить название несохранненой книги")
    @Test
    public void editNonStored() {
        var result = service.rename(new Book(9, UPDATED_TITLE));

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    @DisplayName("удалять книгу, если она сохранена")
    @Test
    public void removeStored() {
        var result = service.remove(INITIAL_BOOK.getId());

        assertThat(result.isOk()).isTrue();
    }

    @DisplayName("отдавать пустой результат при попытке удалить несохраненную книгу")
    @Test
    public void removeNonStored() {
        var result = service.remove(9);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }
}
