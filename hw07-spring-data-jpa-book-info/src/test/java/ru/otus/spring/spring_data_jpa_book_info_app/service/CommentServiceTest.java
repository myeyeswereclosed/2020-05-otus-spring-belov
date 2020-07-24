package ru.otus.spring.spring_data_jpa_book_info_app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.service.comment.CommentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис работы с комментариями должен ")
@Transactional
@SpringBootTest
public class CommentServiceTest {
    private static final Comment INITIAL_COMMENT = new Comment(1, "Good book!");
    private static final String COMMENT_TEXT_CHANGED = "Super book!";

    @Autowired
    private CommentService service;

    @DisplayName("отдавать результат с комментарем, если он найден")
    @Test
    public void findStored() {
        var result = service.find(INITIAL_COMMENT.getId());

        assertThat(result.isOk()).isTrue();

        var maybeComment = result.value();

        assertThat(maybeComment)
            .get()
            .satisfies(
                comment -> {
                    assertThat(comment.getId()).isEqualTo(INITIAL_COMMENT.getId());
                    assertThat(comment.getText()).isEqualTo(INITIAL_COMMENT.getText());
                }
        );
    }

    @DisplayName("отдавать пустой результат, если комментарий не найден")
    @Test
    public void notFound() {
        var result = service.find(0);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    @DisplayName("обновлять текст комментария, если он найден")
    @Test
    public void editStored() {
        var result = service.edit(new Comment(INITIAL_COMMENT.getId(), COMMENT_TEXT_CHANGED));

        assertThat(result.isOk()).isTrue();

        var maybeComment = result.value();

        assertThat(maybeComment)
            .get()
            .satisfies(
                comment -> {
                    assertThat(comment.getId()).isEqualTo(INITIAL_COMMENT.getId());
                    assertThat(comment.getText()).isEqualTo(COMMENT_TEXT_CHANGED);
                }
            );
    }

    @DisplayName("отдавать пустой результат при попытке обновить несохраненный комментарий")
    @Test
    public void editNonStored() {
        var result = service.edit(new Comment(9, COMMENT_TEXT_CHANGED));

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    @DisplayName("удалять комментарий, если он сохранен")
    @Test
    public void removeStored() {
        assertThat(service.findAll().value()).get().extracting(List::size).isEqualTo(1);

        var result = service.remove(INITIAL_COMMENT.getId());

        assertThat(result.isOk()).isTrue();

        assertThat(service.findAll().value())
            .hasValueSatisfying(comments -> assertThat(comments).isEmpty());
    }

    @DisplayName("отдавать пустой результат при попытке удалить несохраненный комментарий")
    @Test
    public void removeNonStored() {
        var result = service.remove(9);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }
}
