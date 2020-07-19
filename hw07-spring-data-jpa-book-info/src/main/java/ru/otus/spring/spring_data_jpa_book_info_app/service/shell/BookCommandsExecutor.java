package ru.otus.spring.spring_data_jpa_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.spring_data_jpa_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.spring_data_jpa_book_info_app.service.book.BookInfoService;
import ru.otus.spring.spring_data_jpa_book_info_app.service.book.BookService;
import ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.OutputFormatter;

import javax.validation.constraints.Size;
import java.util.List;

@ShellComponent
public class BookCommandsExecutor extends BaseCommandExecutor {
    private final BookService bookService;
    private final BookInfoService bookInfoService;
    private final OutputFormatter<Book> bookOutputFormatter;
    private final OutputFormatter<List<Book>> booksOutputFormatter;

    public BookCommandsExecutor(
        BookService service,
        BookInfoService bookInfoService,
        ShellOutputConfig config,
        OutputFormatter<Book> bookOutputFormatter,
        OutputFormatter<List<Book>> booksOutputFormatter
    ) {
        super(config);
        this.bookService = service;
        this.bookInfoService = bookInfoService;
        this.bookOutputFormatter = bookOutputFormatter;
        this.booksOutputFormatter = booksOutputFormatter;
    }

    @ShellMethod(value = "Add book command (only title)", key = {"add_book", "ab"})
    public String add(String title) {
        return
            output(
                bookService.addBook(new Book(title)),
                bookOutputFormatter::format
            );
    }

    @ShellMethod(value = "Rename book", key = {"rename_book", "rb"})
    public String rename(long id, String title) {
        return output(bookService.rename(new Book(id, title)), "Book renamed");
    }

    @ShellMethod(value = "Delete book", key = {"delete_book", "db"})
    public String delete(long id) {
        return output(bookService.remove(id), "Book deleted");
    }

    @ShellMethod(value = "Get all books", key = {"all_books", "all"})
    public String getAll() {
        return output(bookInfoService.getAll(), booksOutputFormatter::format);
    }

    @ShellMethod(value = "Add book author", key = {"add_author", "aa"})
    public String addAuthor(
        long bookId,
        @Size(min = 2, max = 64) String firstName,
        @Size(min = 2, max = 256) String lastName
    ) {
        return
            output(
                bookInfoService.addBookAuthor(bookId, new Author(firstName, lastName)),
                bookOutputFormatter::format
            );
    }

    @ShellMethod(value = "Add book genre", key = {"add_genre", "ag"})
    public String addGenre(long bookId, @Size(min = 2, max = 64) String name) {
        return output(bookInfoService.addBookGenre(bookId, new Genre(name)), bookOutputFormatter::format);
    }

    @ShellMethod(value = "Add book comment", key = {"add_comment", "ac"})
    public String addComment(long bookId, @Size(min = 2) String text) {
        return output(bookInfoService.addComment(bookId, new Comment(text)), bookOutputFormatter::format);
    }

    @ShellMethod(value = "Book info", key = {"book_info", "bi"})
    public String bookInfo(long id) {
        return output(bookInfoService.get(id), bookOutputFormatter::format);
    }
}
