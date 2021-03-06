package ru.otus.spring.jpa_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.jpa_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.service.genre.GenreService;

@ShellComponent
public class GenreCommandsExecutor extends BaseCommandExecutor {
    private final GenreService service;

    public GenreCommandsExecutor(GenreService service, ShellOutputConfig config) {
        super(config);
        this.service = service;
    }

    @ShellMethod(value = "Add new genre", key = {"new_genre", "ng"})
    public String add(String title) {
        return output(service.create(new Genre(title)), Genre::toString);
    }

    @ShellMethod(value = "Edit genre", key = {"edit_genre", "eg"})
    public String edit(int id, String name) {
        return output(service.update(new Genre(id, name)));
    }

    @ShellMethod(value = "Remove genre", key = {"remove_genre", "rg"})
    public String remove(int id) {
        return output(service.remove(id));
    }
}
