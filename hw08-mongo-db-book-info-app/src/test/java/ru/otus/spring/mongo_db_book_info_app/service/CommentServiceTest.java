package ru.otus.spring.mongo_db_book_info_app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;
import ru.otus.spring.mongo_db_book_info_app.service.comment.CommentService;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DisplayName("Сервис работы с комментариями должен ")
@SpringBootTest
public class CommentServiceTest {
    private static final String INITIAL_BOOK_TITLE = "Tri porosenka";
    private static final String INITIAL_COMMENT_TEXT = "Good book!";

    private static final String COMMENT_TEXT_CHANGED = "Super book!";

    @Autowired
    private CommentService service;

    @DisplayName("находить все комментарии")
    @Test
    public void findAll() {
        var result = service.findAll();

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .satisfies(
                comments -> {
                    assertThat(comments.size()).isEqualTo(1);
                    assertThat(comments.get(0).getText()).isEqualTo(INITIAL_COMMENT_TEXT);
                }
            );
    }

    @DisplayName("отдавать результат с комментарем, если он найден")
    @Test
    public void findStored() {
        service
            .findAll()
            .value()
            .ifPresentOrElse(
                comments -> findInitial(comments.get(0).getId()),
                () -> fail("Initial comment not found")
            );
    }

    private void findInitial(String id) {
        var result = service.find(id);

        assertThat(result.isOk()).isTrue();

        assertThat(result.value())
            .get()
            .extracting(Comment::getText)
            .isEqualTo(INITIAL_COMMENT_TEXT);

        assertThat(result.value())
            .get()
            .extracting(Comment::getBook)
            .extracting(Book::getTitle)
            .isEqualTo(INITIAL_BOOK_TITLE)
        ;
    }

    private Optional<Comment> initialComment(String id) {
        return service.find(id).value();
    }

    @DisplayName("отдавать пустой результат, если комментарий не найден")
    @Test
    public void notFound() {
        var result = service.find("");

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    @DisplayName("обновлять текст комментария, если он найден")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void editStored() {
        service
            .findAll()
            .value()
            .ifPresentOrElse(
                comments ->
                    initialComment(comments.get(0).getId())
                        .ifPresentOrElse(
                            this::testCommentChanged,
                            () -> fail("Initial comment not found")
                        ),
                () -> fail("No comments found")
            );
    }

    private void testCommentChanged(Comment comment) {
        assertThat(comment.getText()).isEqualTo(INITIAL_COMMENT_TEXT);

        comment.setText(COMMENT_TEXT_CHANGED);

        var editResult = service.edit(comment);

        assertThat(editResult.isOk()).isTrue();

        var findCommentResult = service.find(comment.getId());

        assertThat(findCommentResult.isOk()).isTrue();
        assertThat(findCommentResult.value()).get().extracting(Comment::getText).isEqualTo(COMMENT_TEXT_CHANGED);
    }

    @DisplayName("отдавать пустой результат при попытке обновить несохраненный комментарий")
    @Test
    public void editNonStored() {
        var result = service.edit(new Comment(COMMENT_TEXT_CHANGED));

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    @DisplayName("удалять комментарий, если он сохранен")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeStored() {
        service
            .findAll()
            .value()
            .ifPresentOrElse(
                comments -> {
                    initialComment(comments.get(0).getId())
                        .ifPresentOrElse(
                            this::testCommentDeleted,
                            () -> fail("Initial comment not found")
                        );
                },
                () -> fail("No comments found")
            );
    }

    private void testCommentDeleted(Comment comment) {
        var result = service.remove(comment.getId());

        assertThat(result.isOk()).isTrue();
        assertThat(service.findAll().value())
            .hasValueSatisfying(comments -> assertThat(comments).isEmpty());
    }

    @DisplayName("отдавать пустой результат при попытке удалить несохраненный комментарий")
    @Test
    public void removeNonStored() {
        var result = service.remove("");

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }
}
