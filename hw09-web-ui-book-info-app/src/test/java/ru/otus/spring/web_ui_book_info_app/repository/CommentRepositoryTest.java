package ru.otus.spring.web_ui_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Comment;
import ru.otus.spring.web_ui_book_info_app.repository.book.BookRepository;
import ru.otus.spring.web_ui_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.web_ui_book_info_app.repository.comment.UpdateCommentConfig;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentScan(
    {
        "ru.otus.spring.web_ui_book_info_app.migration",
        "ru.otus.spring.web_ui_book_info_app.ru.otus.spring.web_ui_book_info_app.repository",
        "ru.otus.spring.web_ui_book_info_app.config"
    }
)
@DisplayName("Репозиторий комментариев должен ")
@DataMongoTest
public class CommentRepositoryTest {
    private final static String INITIAL_BOOK_TITLE = "Tri porosenka";
    private final static String UPDATED_BOOK_TITLE = "Tri kotenka";

    private final static String INITIAL_COMMENT_TEXT = "Good book!";

    private final static String NEW_COMMENT_TEXT = "Terrific";

    private final static String UPDATED_COMMENT_TEXT = "Super book!";

    @Autowired
    private CommentRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("находить все комментарии")
    @Test
    public void findAll() {
        assertInitialComment();
    }

    @DisplayName("сохранять и находить cохраненный комментарий по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void findById() {
        var comment = repository.save(new Comment(NEW_COMMENT_TEXT, initialBook()));

        var maybeComment = repository.findById(comment.getId());

        assertThat(maybeComment).get().isEqualTo(comment);
        assertThat(maybeComment).get().extracting(Comment::getBook).isEqualTo(comment.getBook());
    }

    @DisplayName("отдавать пустой результат, если комментарий не найден")
    @Test
    public void emptyIfNotFound() {
        assertThat(repository.findById("nothingWillBeFound")).isEmpty();
    }

    @DisplayName("обновлять текст комментария")
    @Test
    public void update() {
        assertInitialComment();

        var initialComment = initialComment();
        initialComment.setText(UPDATED_COMMENT_TEXT);

        assertThat(repository.update(initialComment)).isPresent();

        var comments = repository.findAll();

        assertThat(comments.size()).isEqualTo(1);
        assertThat(initialComment().getText()).isEqualTo(UPDATED_COMMENT_TEXT);
    }

    @DisplayName("отдавать пустой результат при обновлении несохраненного комментария")
    @Test
    public void updateNonStored() {
        assertInitialComment();

        assertThat(
            repository.update(new Comment("NonStoredId", UPDATED_COMMENT_TEXT))
        ).isEmpty();
    }

    @DisplayName("удалять комментарий по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void delete() {
        var initialCommentId = initialComment().getId();

        assertThat(repository.delete(initialCommentId)).get().isEqualTo(initialCommentId);
        assertThat(repository.findAll()).isEmpty();
    }

    @DisplayName("не удалять комментарий по несуществующему идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void deleteNonStored() {
        assertInitialComment();

        assertThat(repository.delete("NonStoredId")).isEmpty();

        assertInitialComment();
    }

    @DisplayName("находить все комментарии, относящиеся к книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void findAllByBookId() {
        assertInitialComment();

        var initialBook = initialBook();

        repository.save(new Comment(NEW_COMMENT_TEXT, initialBook));

        var comments = repository.findAllByBook_Id(initialBook.getId());

        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments.get(0).getText()).isEqualTo(INITIAL_COMMENT_TEXT);
        assertThat(comments.get(1).getText()).isEqualTo(NEW_COMMENT_TEXT);
        comments.forEach(
            comment -> assertThat(comment.getBook()).isEqualTo(initialBook)
        );
    }

    @DisplayName("обновлять данные книги во всех комментариях, оставленных к ней")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void updateBookData() {
        assertInitialComment();

        var initialBook = initialBook();

        repository.save(new Comment(NEW_COMMENT_TEXT, initialBook));

        var updatedBook = initialBook.changeTitle(UPDATED_BOOK_TITLE);

        repository.update(UpdateCommentConfig.renameBook(updatedBook));

        var comments = repository.findAll();

        assertThat(comments.size()).isEqualTo(2);
        comments.forEach(
            comment -> assertThat(comment.getBook()).isEqualTo(updatedBook)
        );
    }

    private void assertInitialComment() {
        var comments = repository.findAll();

        assertThat(comments.size()).isEqualTo(1);

        var initialComment = comments.get(0);

        assertThat(initialComment.getText()).isEqualTo(INITIAL_COMMENT_TEXT);
        assertThat(initialComment.getBook()).isEqualTo(initialBook());
    }

    private Book initialBook() {
        var book = bookRepository.findAll().get(0);

        assertThat(book.getTitle()).isEqualTo(INITIAL_BOOK_TITLE);

        return book;
    }

    private Comment initialComment() {
        return repository.findAll().get(0);
    }
}
