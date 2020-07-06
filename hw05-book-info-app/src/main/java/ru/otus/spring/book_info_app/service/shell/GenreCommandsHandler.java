package ru.otus.spring.book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.book_info_app.config.ShellOutputConfig;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.service.genre.GenreService;

@ShellComponent
public class GenreCommandsHandler extends BaseCommandHandler {
    private final GenreService service;

    public GenreCommandsHandler(GenreService service, ShellOutputConfig config) {
        super(config);
        this.service = service;
    }

    @ShellMethod(value = "Add new genre", key = {"new_genre", "ng"})
    public String add(String title) {
        return output(service.create(title), Genre::toString);
    }

    @ShellMethod(value = "Edit genre", key = {"edit_genre", "eg"})
    public String edit(long id, String name) {
        return output(service.update(id, name));
    }

    @ShellMethod(value = "Delete genre", key = {"delete_genre", "dg"})
    public String delete(long id) {
        return output(service.delete(id));
    }
}
