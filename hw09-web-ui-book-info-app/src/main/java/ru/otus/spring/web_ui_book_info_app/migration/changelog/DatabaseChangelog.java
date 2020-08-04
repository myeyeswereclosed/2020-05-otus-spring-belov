package ru.otus.spring.web_ui_book_info_app.migration.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Comment;

@ChangeLog
public class DatabaseChangelog {
    private static final String INITIAL_BOOK_TITLE = "Tri kotenka";
    private final static String INITIAL_COMMENT_TEXT = "Good book!";

    private Book initialBook;

    @ChangeSet(order = "001", id = "dropBookDb", author = "belov", runAlways = true)
    public void drop(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "002", id = "initBook", author = "belov", runAlways = true)
    public void initBook(MongoTemplate template) {
        initialBook = template.save(new Book(INITIAL_BOOK_TITLE));
    }

    @ChangeSet(order = "003", id = "initComment", author = "belov", runAlways = true)
    public void initComment(MongoTemplate template) {
        template.save(new Comment(INITIAL_COMMENT_TEXT, initialBook));
    }
}
