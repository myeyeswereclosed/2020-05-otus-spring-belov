package ru.otus.spring.book_info_app.service.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.book_info_app.config.ShellOutputConfig;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.BookInfo;
import ru.otus.spring.book_info_app.service.book.BookService;
import ru.otus.spring.book_info_app.service.book_info.BookInfoService;
import ru.otus.spring.book_info_app.service.name_parser.NameParser;
import ru.otus.spring.book_info_app.service.shell.session.ShellSessionHandlerImpl;

import javax.validation.constraints.Pattern;
import java.util.List;

@ShellComponent
public class BookCommandsHandler extends BaseCommandHandler {
    private final BookService bookService;
    private final BookInfoService bookInfoService;
    private final NameParser nameParser;
    private final ShellSessionHandlerImpl sessionHandler;

    public BookCommandsHandler(
        BookService service,
        BookInfoService bookInfoService,
        ShellSessionHandlerImpl sessionStorage,
        ShellOutputConfig config,
        NameParser nameParser
    ) {
        super(config);
        this.bookService = service;
        this.bookInfoService = bookInfoService;
        this.sessionHandler = sessionStorage;
        this.nameParser = nameParser;
    }

    @ShellMethod(value = "Add book command (only title)", key = {"add_book", "ab"})
    @ShellMethodAvailability(value = "newBookCreationAllowed")
    public String add(String title) {
        return
            output(
                sessionHandler.startSession(title),
                (book -> book.toString() + " added")
            );
    }

    @ShellMethod(value = "Get book by id", key = {"get_book", "gb"})
    public String get(long id) {
        return output(bookService.find(id), Book::toString);
    }

    @ShellMethod(value = "Rename book", key = {"rename_book", "rb"})
    public String rename(long id, String title) {
        return
            output(
                bookService.rename(id, title),
                Book::toString
            );
    }

    @ShellMethod(value = "Delete book", key = {"delete_book", "db"})
    public String delete(long id) {
        return output(sessionHandler.removeBook(id));
    }

    @ShellMethod(value = "Get all books", key = {"all_books", "all"})
    public String getAll() {
        return output(bookService.getAll(), List::toString);
    }

    @ShellMethod(value = "Add book author", key = {"add_author", "aa"})
    @ShellMethodAvailability(value = "bookInfoAllowed")
    public String addAuthor(@Pattern(regexp = NAME_PATTERN) String name) {
        return output(sessionHandler.addBookAuthor(nameParser.parse(name)));
    }

    @ShellMethod(value = "Add book genre", key = {"add_genre", "ag"})
    @ShellMethodAvailability(value = "bookInfoAllowed")
    public String addGenre(String name) {
        return output(sessionHandler.addBookGenre(name));
    }

    @ShellMethod(value = "Book info", key = {"book_info", "bi"})
    public String bookInfo(long id) {
        return output(bookInfoService.get(id), BookInfo::toString);
    }

    private Availability newBookCreationAllowed() {
        return sessionHandler.canStartSession();
    }

    private Availability bookInfoAllowed() {
        return sessionHandler.canAddBookInfo();
    }
}
