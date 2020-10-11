package ru.otus.spring.app_container.migration.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.app_container.domain.Author;
import ru.otus.spring.app_container.domain.Book;
import ru.otus.spring.app_container.domain.Comment;
import ru.otus.spring.app_container.domain.Genre;

@ChangeLog
public class DatabaseChangelog {
    private static final String INITIAL_BOOK_TITLE = "Tri kotenka";
    private final static String INITIAL_COMMENT_TEXT = "Good book!";
    private final static Genre INITIAL_GENRE = new Genre("horror");
    private final static Author INITIAL_AUTHOR = new Author("Some", "Author");

    private Book initialBook;

    @ChangeSet(order = "001", id = "dropBookDb", author = "belov", runAlways = true)
    public void drop(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "002", id = "initBook", author = "belov", runAlways = true)
    public void initBook(MongoTemplate mongo) {
        initialBook = mongo.save(new Book(INITIAL_BOOK_TITLE));
    }

    @ChangeSet(order = "003", id = "initComment", author = "belov", runAlways = true)
    public void initComment(MongoTemplate mongo) {
        mongo.save(new Comment(INITIAL_COMMENT_TEXT, initialBook));
    }

    @ChangeSet(order = "004", id = "initGenre", author = "belov", runAlways = true)
    public void initGenre(MongoTemplate mongo) {
        mongo.save(INITIAL_GENRE);
    }

    @ChangeSet(order = "005", id = "initAuthor", author = "belov", runAlways = true)
    public void initAuthor(MongoTemplate mongo) {
        mongo.save(INITIAL_AUTHOR);
    }
}
