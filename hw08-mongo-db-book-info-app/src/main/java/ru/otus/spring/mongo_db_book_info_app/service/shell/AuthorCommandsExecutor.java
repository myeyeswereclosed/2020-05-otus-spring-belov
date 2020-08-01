package ru.otus.spring.mongo_db_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.mongo_db_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.service.author.AuthorService;
import ru.otus.spring.mongo_db_book_info_app.service.book.info.edit.EditBookInfoService;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.OperationResultFormatter;

import javax.validation.constraints.Size;

@ShellComponent
public class AuthorCommandsExecutor extends BaseCommandExecutor {
    private final AuthorService service;
    private final EditBookInfoService bookInfoService;
    private final OperationResultFormatter<Author> formatter;

    public AuthorCommandsExecutor(
        AuthorService service,
        EditBookInfoService bookInfoService,
        ShellOutputConfig config,
        OperationResultFormatter<Author> formatter
    ) {
        super(config);
        this.service = service;
        this.bookInfoService = bookInfoService;
        this.formatter = formatter;
    }

    @ShellMethod(value = "Add new author", key = {"new_author", "na"})
    public String add(
        @Size(min = 2, max = 64) String firstName,
        @Size(min = 2, max = 256) String lastName
    ) {
        return output(service.create(new Author(firstName, lastName)), formatter::info);
    }

    @ShellMethod(value = "Edit author", key = {"edit_author", "ea"})
    public String edit(
        String id,
        @Size(min = 2, max = 64) String firstName,
        @Size(min = 2, max = 256) String lastName
    ) {
        return output(bookInfoService.renameAuthor(new Author(id, firstName, lastName)), formatter::editInfo);
    }

    @ShellMethod(value = "Remove author", key = {"remove_author", "ra"})
    public String delete(String id) {
        return output(bookInfoService.removeAuthorById(id), formatter::removeInfo);
    }
}
