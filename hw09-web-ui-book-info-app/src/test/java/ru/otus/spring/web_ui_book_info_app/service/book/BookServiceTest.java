package ru.otus.spring.web_ui_book_info_app.service.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Comment;
import ru.otus.spring.web_ui_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.web_ui_book_info_app.service.result.ServiceResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DisplayName("Сервис работы с книгами должен ")
@SpringBootTest
public class BookServiceTest {
    private static final String NEW_BOOK_TITLE = "Tri kotenka";

    private static final String UPDATED_TITLE = "Tri utenka";

    private static final String FIRST_COMMENT_TEXT = "Good!";
    private static final String SECOND_COMMENT_TEXT = "Super!";

    @Autowired
    private BookService service;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("сохранять новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addBook() {
        var result = service.addBook(new Book(NEW_BOOK_TITLE));

        assertNewBookAdded(result);
    }

    private void assertNewBookAdded(ServiceResult<Book> result) {
        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).get().extracting(Book::getTitle).isEqualTo(NEW_BOOK_TITLE);
        assertThat(result.value()).get().extracting(Book::getId).isNotNull();
    }

    @DisplayName("обновлять название хранимой книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void editStored() {
        var storeResult = service.addBook(new Book(NEW_BOOK_TITLE));

        assertNewBookAdded(storeResult);

        storeResult
            .value()
            .ifPresentOrElse(
                newBook -> {
                    var result = service.rename(new Book(newBook.getId(), UPDATED_TITLE));

                    assertNewBookUpdated(result, newBook.getId());
                },
                () -> fail("Book not added")
            );
    }

    @DisplayName("обновлять название хранимой книги в комментариях к ней")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void editStoredBookTitleInComments() {
        var storeResult = service.addBook(new Book(NEW_BOOK_TITLE));

        assertNewBookAdded(storeResult);

        storeResult
            .value()
            .ifPresentOrElse(
                newBook -> {
                    // only initial comment
                    assertThat(commentRepository.findAll().size()).isEqualTo(1);

                    var firstComment = commentRepository.save(new Comment(FIRST_COMMENT_TEXT, newBook));
                    var secondComment = commentRepository.save(new Comment(SECOND_COMMENT_TEXT, newBook));

                    var book = new Book(newBook.getId(), UPDATED_TITLE);

                    var result = service.rename(book);

                    assertNewBookUpdated(result, newBook.getId());

                    assertCommentsUpdated(
                        commentRepository.findAllByBook_Id(newBook.getId()),
                        List.of(firstComment, secondComment),
                        book
                    );
                },
                () -> fail("Book not added")
            );
    }

    private void assertCommentsUpdated(List<Comment> comments, List<Comment> expectedComments, Book expectedBook) {
        var commentsNumber = comments.size();

        assertThat(commentsNumber).isEqualTo(expectedComments.size());

        for (var i = 0; i < commentsNumber; i++) {
            assertThat(comments.get(i).getBook()).isEqualTo(expectedBook);
            assertThat(comments.get(i).getId()).isEqualTo(expectedComments.get(i).getId());
            assertThat(comments.get(i).getText()).isEqualTo(expectedComments.get(i).getText());
        }
    }

    private void assertNewBookUpdated(ServiceResult<Book> updateResult, String bookId) {
        assertThat(updateResult.isOk()).isTrue();

        var maybeBook = updateResult.value();

        assertThat(maybeBook).get().satisfies(
            book -> {
                assertThat(book.getId()).isEqualTo(bookId);
                assertThat(book.getTitle()).isEqualTo(UPDATED_TITLE);
            }
        );
    }

    @DisplayName("отдавать пустой результат при попытке обновить название несохранненой книги")
    @Test
    public void editNonStored() {
        var result = service.rename(new Book(UPDATED_TITLE));

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    @DisplayName("удалять книгу, если она сохранена, ")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeStored() {
        var addResult = service.addBook(new Book(NEW_BOOK_TITLE));

        assertThat(addResult.isOk()).isTrue();

        addResult
            .value()
            .ifPresentOrElse(
                book -> {
                    var removeResult = service.remove(book.getId());

                    assertThat(removeResult.isOk()).isTrue();
                    assertThat(removeResult.value()).get().isEqualTo(book.getId());
                    assertThat(commentRepository.findAllByBook_Id(book.getId())).isEmpty();
                },
                () -> fail("Book was not added")
            );
    }

    @DisplayName("отдавать пустой результат при попытке удалить несохраненную книгу")
    @Test
    public void removeNonStored() {
        var result = service.remove("");

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }
}
