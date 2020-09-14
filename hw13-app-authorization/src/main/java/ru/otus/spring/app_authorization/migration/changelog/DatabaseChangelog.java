package ru.otus.spring.app_authorization.migration.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.app_authorization.domain.Book;
import ru.otus.spring.app_authorization.domain.Comment;
import ru.otus.spring.app_authorization.security.user.AppUser;
import ru.otus.spring.app_authorization.security.user.Role;

@ChangeLog
public class DatabaseChangelog {
    private static final String INITIAL_BOOK_TITLE = "Tri kotenka";
    private final static String INITIAL_COMMENT_TEXT = "Good book!";

    // admin -> mysecret
    private final static AppUser admin =
        new AppUser(
            "admin",
            "$2y$12$etbm5cXctCnzyE/GjruePOU5jrrhhlgfBUJIbhw5Ag7fyyZ1m5kN2",
            "Admin",
            "1",
            Role.ADMIN
        );

    // manager -> testsecret
    private final static AppUser manager =
        new AppUser(
            "manager",
            "$2y$12$rPNWNUK1/eo/2SHoEro4hOsLKMOUoLt5VzJDFpW4jI0i6HfyISQ9C",
            "Super",
            "Manager",
            Role.MANAGER
        );

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

    @ChangeSet(order = "004", id = "initAdmin", author = "belov", runAlways = true)
    public void initAdmin(MongoTemplate template) {
        template.save(admin);
    }

    @ChangeSet(order = "005", id = "initManager", author = "belov", runAlways = true)
    public void initManager(MongoTemplate template) {
        template.save(manager);
    }
}
