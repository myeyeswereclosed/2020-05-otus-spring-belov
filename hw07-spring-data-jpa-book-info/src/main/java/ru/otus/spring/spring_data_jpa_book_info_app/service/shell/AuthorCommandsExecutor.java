package ru.otus.spring.spring_data_jpa_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.spring_data_jpa_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.service.author.AuthorService;

import javax.validation.constraints.Size;

@ShellComponent
public class AuthorCommandsExecutor extends BaseCommandExecutor {
    private final AuthorService service;

    public AuthorCommandsExecutor(AuthorService service, ShellOutputConfig config) {
        super(config);
        this.service = service;
    }

    @ShellMethod(value = "Add new author", key = {"new_author", "na"})
    public String add(
        @Size(min = 2, max = 64) String firstName,
        @Size(min = 2, max = 256) String lastName
    ) {
        return
            output(
                service.create(new Author(firstName, lastName)),
                (author -> author.toString() + " added")
            );
    }

    @ShellMethod(value = "Edit author", key = {"edit_author", "ea"})
    public String edit(
        long id,
        @Size(min = 2, max = 64) String firstName,
        @Size(min = 2, max = 256) String lastName
    ) {
        return output(service.update(new Author(id, firstName, lastName)), "Author renamed");
    }

    @ShellMethod(value = "Delete author", key = {"delete_author", "da"})
    public String delete(long id) {
        return output(service.remove(id), "Author deleted");
    }
}
