package ru.otus.spring.mongo_db_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.mongo_db_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.service.book.BookService;
import ru.otus.spring.mongo_db_book_info_app.service.book.info.add.AddBookInfoService;
import ru.otus.spring.mongo_db_book_info_app.service.book.info.get.GetBookInfoService;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.OutputFormatter;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.book.BookOperationFormatter;

import javax.validation.constraints.Size;

@ShellComponent
public class BookCommandsExecutor extends BaseCommandExecutor {
    private final BookService bookService;
    private final GetBookInfoService bookInfoService;
    private final AddBookInfoService addBookInfoService;
    private final BookOperationFormatter formatter;
    private final OutputFormatter<Comment> commentOutputFormatter;

    public BookCommandsExecutor(
        BookService service,
        GetBookInfoService bookInfoService,
        ShellOutputConfig config,
        AddBookInfoService addBookInfoService, BookOperationFormatter formatter, OutputFormatter<Comment> commentOutputFormatter
    ) {
        super(config);
        this.bookService = service;
        this.bookInfoService = bookInfoService;
        this.addBookInfoService = addBookInfoService;
        this.formatter = formatter;
        this.commentOutputFormatter = commentOutputFormatter;
    }

    @ShellMethod(value = "Add book command (only title)", key = {"add_book", "ab"})
    public String add(String title) {
        return
            output(
                bookService.addBook(new Book(title)),
                book -> formatter.info(book.toInfo())
            );
    }

    @ShellMethod(value = "Rename book", key = {"rename_book", "rb"})
    public String rename(String id, String title) {
        return
            output(
                bookService.rename(new Book(id, title)),
                book -> formatter.editInfo(book.toInfo())
            );
    }

    @ShellMethod(value = "Delete book", key = {"delete_book", "db"})
    public String delete(String id) {
        return output(bookService.remove(id), formatter::removeInfo);
    }

    @ShellMethod(value = "Get all books", key = {"all_books", "all"})
    public String getAll() {
        return output(bookInfoService.getAll(), formatter::listInfo);
    }

    @ShellMethod(value = "Add book author", key = {"add_author", "aa"})
    public String addAuthor(
        String bookId,
        @Size(min = 2, max = 64) String firstName,
        @Size(min = 2, max = 256) String lastName
    ) {
        return
            output(
                addBookInfoService.addBookAuthor(bookId, new Author(firstName, lastName)),
                book -> formatter.info(book.toInfo())
            );
    }

    @ShellMethod(value = "Add book genre", key = {"add_genre", "ag"})
    public String addGenre(String bookId, @Size(min = 2, max = 64) String name) {
        return
            output(
                addBookInfoService.addBookGenre(bookId, new Genre(name)),
                book -> formatter.info(book.toInfo())
            );
    }

    @ShellMethod(value = "Add book comment", key = {"add_comment", "ac"})
    public String addComment(String bookId, @Size(min = 2) String text) {
        return
            output(
                addBookInfoService.addComment(bookId, new Comment(text)),
                commentOutputFormatter::format
            );
    }

    @ShellMethod(value = "Book info", key = {"book_info", "bi"})
    public String bookInfo(String id) {
        return output(bookInfoService.get(id), formatter::info);
    }
}
