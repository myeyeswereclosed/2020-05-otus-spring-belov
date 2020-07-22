package ru.otus.spring.spring_data_jpa_book_info_app.service.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.spring_data_jpa_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.spring_data_jpa_book_info_app.dto.BookInfo;
import ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.OutputFormatter;
import ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.book.BookOperationFormatter;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Shell сервис работы с книгами должен ")
@SpringBootTest
public class BookCommandExecutorTest {
    private static final Author INITIAL_AUTHOR = new Author(1, "Some", "Author");
    private static final Genre INITIAL_GENRE = new Genre(1, "horror");

    private static final Book INITIAL_BOOK =
        new Book(1, "Tri porosenka", Set.of(INITIAL_AUTHOR), Set.of(INITIAL_GENRE));

    private static final Comment INITIAL_COMMENT = new Comment(1, "Good book!", INITIAL_BOOK);

    private static final Book SECOND_BOOK = new Book(2, "Tri_kotenka");
    private static final Book THIRD_BOOK = new Book(3, "Tri_utenka");

    // book info
    private static final List<String> BOOK_INFO_COMMANDS = Arrays.asList("book_info 1", "bi 1");
    private static final String NON_STORED_BOOK_INFO_COMMAND = "book_info 9";

    // add book
    private static final Map<String, Book> ADD_BOOK_COMMANDS =
        new HashMap<>() {{
            put("ab " + SECOND_BOOK.getTitle(), SECOND_BOOK);
            put("add_book " + THIRD_BOOK.getTitle(), THIRD_BOOK);
        }};

    // add author
    private static final Author NEW_AUTHOR = new Author(2, "Oleg", "Kotov");
    private static final List<String> ADD_AUTHOR_COMMANDS =
        Arrays.asList(
            "aa 1 " + NEW_AUTHOR.getFirstName() + " " + NEW_AUTHOR.getLastName(),
            "add_author 1 " + INITIAL_AUTHOR.getFirstName() + " " + INITIAL_AUTHOR.getLastName()
        );
    private static final String ADD_NON_STORED_BOOK_AUTHOR_COMMAND = "aa 9 Some Author";

    // add genre
    private static final Genre NEW_GENRE = new Genre(3, "drama");
    private static final List<String> ADD_GENRE_COMMANDS =
        Arrays.asList(
            "ag 1 " + NEW_GENRE.getName(),
            "add_genre 1 " + NEW_GENRE.getName()
        );
    private static final String ADD_NON_STORED_BOOK_GENRE_COMMAND = "ag 9 fail";

    // add comment
    private static final Comment SECOND_COMMENT = new Comment(2, "Bad!", INITIAL_BOOK);
    private static final Comment THIRD_COMMENT = new Comment(3, "Terrific", INITIAL_BOOK);
    private static final Map<String, Comment> ADD_COMMENT_COMMANDS =
        new HashMap<>() {{
            put("ac 1 " + SECOND_COMMENT.getText(), SECOND_COMMENT);
            put("add_comment 1 " + THIRD_COMMENT.getText(), THIRD_COMMENT);
        }};
    private static final String ADD_NON_STORED_BOOK_COMMENT_COMMAND = "ac 9 great";

    // rename
    private static final Map<String, String> RENAME_BOOK_COMMANDS =
        new HashMap<>() {{
            put("rb 1 " + SECOND_BOOK.getTitle(), SECOND_BOOK.getTitle());
            put("rename_book 1 " + THIRD_BOOK.getTitle(), THIRD_BOOK.getTitle());
        }};
    private static final String RENAME_NON_STORED_BOOK_COMMAND = "rb 9 empty";

    // remove
    private static final String FULL_REMOVE_COMMAND = "delete_book 1";
    private static final String SHORT_REMOVE_COMMAND = "db 1";
    private static final String REMOVE_NON_STORED_BOOK_COMMAND = "db 9";

    @Autowired
    private Shell shell;

    @Autowired
    private BookOperationFormatter formatter;

    @Autowired
    private OutputFormatter<Comment> commentFormatter;

    @Autowired
    private ShellOutputConfig config;

    @DisplayName("выводить информацию о книге")
    @Test
    public void info() {
        BOOK_INFO_COMMANDS.forEach(
            command -> {
                var output = (String) shell.evaluate(() -> command);

                assertThat(output)
                    .isNotEmpty()
                    .isEqualTo(formatter.info(new BookInfo(INITIAL_BOOK, Set.of(INITIAL_COMMENT))))
                ;
            }
        );
    }

    @DisplayName("cooбщать, что книга не найдена")
    @Test
    public void bookNotFound() {
        var output = (String) shell.evaluate(() -> NON_STORED_BOOK_INFO_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }

    @DisplayName("добавлять новую книгу")
    @Test
    public void addNewBook() {
        ADD_BOOK_COMMANDS.forEach(
            (command, expectedBook) -> {
                var output = (String) shell.evaluate(() -> command);

                assertThat(output)
                    .isNotEmpty()
                    .isEqualTo(formatter.info(expectedBook.toInfo()))
                ;
            }
        );
    }

    @DisplayName("добавлять авторов книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addAuthor() {
        var expectedBook =
            new Book(
                INITIAL_BOOK.getId(),
                INITIAL_BOOK.getTitle(),
                Set.of(INITIAL_AUTHOR, NEW_AUTHOR),
                INITIAL_BOOK.getGenres()
            );

        ADD_AUTHOR_COMMANDS.forEach(
            command -> {
                var output = (String) shell.evaluate(() -> command);

                assertThat(output)
                    .isNotEmpty()
                    .isEqualTo(formatter.info(expectedBook.toInfo()))
                ;
            }
        );
    }

    @DisplayName("сообщать, что книга не найдена, при добавлении автора")
    @Test
    public void addAuthorOfNonStoredBook() {
        var output = (String) shell.evaluate(() -> ADD_NON_STORED_BOOK_AUTHOR_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }

    @DisplayName("добавлять жанры книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addGenre() {
        var expectedBook =
            new Book(
                INITIAL_BOOK.getId(),
                INITIAL_BOOK.getTitle(),
                INITIAL_BOOK.getAuthors(),
                Set.of(INITIAL_GENRE, NEW_GENRE)
            );

        ADD_GENRE_COMMANDS.forEach(
            command -> {
                var output = (String) shell.evaluate(() -> command);

                assertThat(output)
                    .isNotEmpty()
                    .isEqualTo(formatter.info(expectedBook.toInfo()))
                ;
            }
        );
    }

    @DisplayName("сообщать, что книга не найдена, при добавлении жанра")
    @Test
    public void addGenreOfNonStoredBook() {
        var output = (String) shell.evaluate(() -> ADD_NON_STORED_BOOK_GENRE_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }

    @DisplayName("добавлять комментарии к книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addComment() {
        ADD_COMMENT_COMMANDS.forEach(
            (command, expectedComment) -> {
                var output = (String) shell.evaluate(() -> command);

                assertThat(output)
                    .isNotEmpty()
                    .isEqualTo(commentFormatter.format(expectedComment))
                ;
            }
        );
    }

    @DisplayName("сообщать, что книга не найдена, при добавлении комментария")
    @Test
    public void addCommentOfNonStoredBook() {
        var output = (String) shell.evaluate(() -> ADD_NON_STORED_BOOK_COMMENT_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }

    @DisplayName("переименовывать книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void rename() {
        RENAME_BOOK_COMMANDS.forEach(
            (command, expectedTitle) -> {
                var expectedBook =
                    new Book(INITIAL_BOOK.getId(), expectedTitle, INITIAL_BOOK.getAuthors(), INITIAL_BOOK.getGenres());

                var output = (String) shell.evaluate(() -> command);

                assertThat(output)
                    .isNotEmpty()
                    .isEqualTo(formatter.editInfo(expectedBook.toInfo()))
                ;
            }
        );
    }

    @DisplayName("сообщать, что книга не найдена, при попытке переименовать")
    @Test
    public void renameNonStoredBook() {
        var output = (String) shell.evaluate(() -> RENAME_NON_STORED_BOOK_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }

    @DisplayName("удалять хранимую книгу (полная версия команды)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeByFullCommand() {
        var output = (String) shell.evaluate(() -> FULL_REMOVE_COMMAND);

        assertThat(output)
            .isNotEmpty()
            .isEqualTo(formatter.removeInfo(INITIAL_BOOK.getId()))
        ;
    }

    @DisplayName("удалять хранимую книгу (краткая версия команды)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeByShortCommand() {
        var output = (String) shell.evaluate(() -> SHORT_REMOVE_COMMAND);

        assertThat(output)
            .isNotEmpty()
            .isEqualTo(formatter.removeInfo(INITIAL_BOOK.getId()))
        ;
    }

    @DisplayName("сообщать, что книга не найдена, при попытке ее удалить")
    @Test
    public void removeNonStoredBook() {
        var output = (String) shell.evaluate(() -> REMOVE_NON_STORED_BOOK_COMMAND);

        assertThat(output).isNotEmpty().isEqualTo(config.getNotFoundMessage());
    }
}
