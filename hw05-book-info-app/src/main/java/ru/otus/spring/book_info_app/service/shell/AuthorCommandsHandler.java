package ru.otus.spring.book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.book_info_app.config.ShellOutputConfig;
import ru.otus.spring.book_info_app.service.author.AuthorService;
import ru.otus.spring.book_info_app.service.name_parser.NameParser;

import javax.validation.constraints.Pattern;

@ShellComponent
public class AuthorCommandsHandler extends BaseCommandHandler {
    private final AuthorService service;
    private final NameParser parser;

    public AuthorCommandsHandler(AuthorService service, ShellOutputConfig config, NameParser parser) {
        super(config);
        this.service = service;
        this.parser = parser;
    }

    @ShellMethod(value = "Add new author", key = {"new_author", "na"})
    public String add(@Pattern(regexp = NAME_PATTERN) String name) {
        return
            output(
                service.create(parser.parse(name)),
                (author -> author.toString() + " added")
            );
    }

    @ShellMethod(value = "Edit author", key = {"edit_author", "ea"})
    public String edit(long id, @Pattern(regexp = NAME_PATTERN) String name) {
        return output(service.update(id, name), "Author renamed");
    }

    @ShellMethod(value = "Delete author", key = {"delete_author", "da"})
    public String delete(long id) {
        return output(service.delete(id), "Author deleted");
    }
}
