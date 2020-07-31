package ru.otus.spring.mongo_db_book_info_app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.dto.BookInfo;
import ru.otus.spring.mongo_db_book_info_app.service.book.get.GetBookInfoService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DisplayName("Сервис получения сводной информации о книгах должен ")
@SpringBootTest
public class GetBookInfoServiceTest {
    private final static String INITIAL_BOOK_NOT_FOUND_MESSAGE = "Initial book not found";

    private static final String INITIAL_BOOK_TITLE = "Tri porosenka";
    private static final String INITIAL_COMMENT_TEXT = "Good book!";

    private static final Author INITIAL_AUTHOR = new Author("Some", "Author");

    private static final String NEW_GENRE_NAME = "love-story";

    @Autowired
    private GetBookInfoService service;

    @DisplayName("находить книгу по идентификатору")
    @Test
    public void get() {
        findInitial()
            .ifPresentOrElse(
                this::assertInitialBookInfo,
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private Optional<BookInfo> findInitial() {
        return service.getAll().value().map(bookInfoList -> bookInfoList.get(0));
    }

    @DisplayName("отдавать пустой результат, если книга не найдена")
    @Test
    public void emptyIfNotFound() {
        var result = service.get("");

        assertThat(result.isOk());
        assertThat(result.value().isEmpty()).isTrue();
    }

    @DisplayName("находить все книги с информацией по ним")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void getAll() {
        var result = service.getAll();

        assertThat(result.isOk()).isTrue();

        assertThat(result.value()).get().extracting(List::size).isEqualTo(1);
        assertThat(result.value())
            .get()
            .satisfies(
                bookInfoList -> {
                    assertThat(bookInfoList.size()).isEqualTo(1);
                    assertInitialBookInfo(bookInfoList.get(0));
                }
            );
    }

    private void assertInitialBookInfo(BookInfo bookInfo) {
        assertInitialBook(bookInfo.getBook());

        assertThat(bookInfo.getComments().size()).isEqualTo(1);
        assertThat(bookInfo.getComments().get(0).getText()).isEqualTo(INITIAL_COMMENT_TEXT);
    }

    private void assertInitialBook(Book book) {
        assertThat(book.getTitle()).isEqualTo(INITIAL_BOOK_TITLE);

        assertThat(book.getAuthors().size()).isEqualTo(1);
        assertThat(book.getAuthors().get(0)).satisfies(
            author -> assertThat(
                author.hasFirstAndLastName(
                    INITIAL_AUTHOR.getFirstName(),
                    INITIAL_AUTHOR.getLastName()
                )
            )
        );

        assertThat(book.getGenres().size()).isEqualTo(1);
        assertThat(book.getGenres().get(0))
            .satisfies(genre -> assertThat(genre.hasName(NEW_GENRE_NAME)));
    }
}
