package ru.otus.spring.spring_data_jpa_book_info_app.service.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.spring_data_jpa_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.comment.CommentOperationFormatter;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Shell сервис работы с комментариями должен ")
@SpringBootTest
public class CommentCommandExecutorTest {
    private static final Book INITIAL_BOOK = new Book(1, "Tri porosenka");
    private static final Comment INITIAL_COMMENT = new Comment(1, "Good book!", INITIAL_BOOK);
    private static final String COMMENT_TEXT_CHANGED = "Super!";

    // get
    private static final List<String> GET_COMMENT_COMMANDS = Arrays.asList("get_comment 1", "gc 1");
    private static final String GET_NON_STORED_COMMENT_COMMAND = "get_comment 9";

    // edit
    private static final List<String> EDIT_COMMENT_COMMANDS =
        Arrays.asList(
            "edit_comment 1 " + COMMENT_TEXT_CHANGED,
            "ec 1 " + COMMENT_TEXT_CHANGED
        );
    private static final String EDIT_NON_STORED_COMMENT_COMMAND = "edit_comment 9 not_edited";

    // remove
    private static final String FULL_REMOVE_COMMAND = "remove_comment 1";
    private static final String SHORT_REMOVE_COMMAND = "rc 1";
    private static final String REMOVE_NON_STORED_COMMENT_COMMAND = "remove_comment 9";

    // get all
    private static final List<String> GET_ALL_COMMENTS_COMMANDS = Arrays.asList("get_all_comments", "gac");

    @Autowired
    private Shell shell;

    @Autowired
    private CommentOperationFormatter formatter;

    @Autowired
    private ShellOutputConfig config;

    @DisplayName("Выводить комментарий, если он сохранен")
    @Test
    public void find() {
        GET_COMMENT_COMMANDS.forEach(
            command -> {
                var output = (String) shell.evaluate(() -> command);

                assertThat(output).isEqualTo(formatter.info(INITIAL_COMMENT));
            }
        );
    }

    @DisplayName("Сообщать, что комментарий не найден")
    @Test
    public void commentNotFound() {
        var output = (String) shell.evaluate(() -> GET_NON_STORED_COMMENT_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }

    @DisplayName("Редактировать комментарий, если он сохранен")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void edit() {
        EDIT_COMMENT_COMMANDS.forEach(
            command -> {
                var output = (String) shell.evaluate(() -> command);

                assertThat(output).isEqualTo(formatter.editInfo(new Comment(COMMENT_TEXT_CHANGED)));
            }
        );
    }

    @DisplayName("Сообщать, что комментарий не найден, при редактировании")
    @Test
    public void editNonStoredComment() {
        var output = (String) shell.evaluate(() -> EDIT_NON_STORED_COMMENT_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }

    @DisplayName("Удалять комментарий (полная версия команды), если он сохранен")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeWithFullCommand() {
        var output = (String) shell.evaluate(() -> FULL_REMOVE_COMMAND);

        assertThat(output).isEqualTo(formatter.removeInfo(INITIAL_COMMENT.getId()));
    }

    @DisplayName("Удалять комментарий (краткая версия команды), если он сохранен")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeWithShortCommand() {
        var output = (String) shell.evaluate(() -> SHORT_REMOVE_COMMAND);

        assertThat(output).isEqualTo(formatter.removeInfo(INITIAL_COMMENT.getId()));
    }

    @DisplayName("Сообщать, что комментарий не найден, при удалении")
    @Test
    public void editCommentNotFound() {
        var output = (String) shell.evaluate(() -> REMOVE_NON_STORED_COMMENT_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }

    @DisplayName("Выводить все комментарии")
    @Test
    public void getAll() {
        GET_ALL_COMMENTS_COMMANDS.forEach(
            command -> {
                var output = (String) shell.evaluate(() -> command);

                assertThat(output).isNotEmpty().isEqualTo(formatter.listInfo(List.of(INITIAL_COMMENT)));
            }
        );
    }
}
