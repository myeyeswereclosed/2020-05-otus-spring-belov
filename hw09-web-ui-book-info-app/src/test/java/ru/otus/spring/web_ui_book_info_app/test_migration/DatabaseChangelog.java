package ru.otus.spring.web_ui_book_info_app.test_migration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Comment;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;

@ChangeLog
public class DatabaseChangelog {
    private static final String INITIAL_BOOK_TITLE = "Tri porosenka";
    private final static String INITIAL_COMMENT_TEXT = "Good book!";
    private final static Author INITIAL_AUTHOR = new Author("Some", "Author");
    private final static Genre INITIAL_GENRE = new Genre("horror");

    private Author initialAuthor;
    private Genre initialGenre;
    private Book initialBook;

    @ChangeSet(order = "001", id = "dropBookDb", author = "belov", runAlways = true)
    public void drop(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "002", id = "initAuthor", author = "belov", runAlways = true)
    public void initAuthor(MongoTemplate template) {
        initialAuthor = template.save(INITIAL_AUTHOR);
    }

    @ChangeSet(order = "003", id = "initGenre", author = "belov", runAlways = true)
    public void initGenre(MongoTemplate template) {
        initialGenre = template.save(INITIAL_GENRE);
    }

    @ChangeSet(order = "004", id = "initBook", author = "belov", runAlways = true)
    public void initBook(MongoTemplate template) {
        initialBook =
            template.save(
                new Book(INITIAL_BOOK_TITLE)
                    .addAuthor(initialAuthor)
                    .addGenre(initialGenre)
            );
    }

    @ChangeSet(order = "005", id = "initComment", author = "belov", runAlways = true)
    public void initComment(MongoTemplate template) {
        template.save(new Comment(INITIAL_COMMENT_TEXT, initialBook));
    }
}
