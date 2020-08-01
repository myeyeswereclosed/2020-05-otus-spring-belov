package ru.otus.spring.mongo_db_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.mongo_db_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.service.book.info.edit.EditBookInfoService;
import ru.otus.spring.mongo_db_book_info_app.service.genre.GenreService;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.genre.GenreOperationFormatter;

@ShellComponent
public class GenreCommandsExecutor extends BaseCommandExecutor {
    private final GenreService service;
    private final EditBookInfoService bookInfoService;
    private final GenreOperationFormatter formatter;

    public GenreCommandsExecutor(
        GenreService service,
        ShellOutputConfig config,
        EditBookInfoService bookInfoService, GenreOperationFormatter formatter
    ) {
        super(config);
        this.service = service;
        this.bookInfoService = bookInfoService;
        this.formatter = formatter;
    }

    @ShellMethod(value = "Add new genre", key = {"new_genre", "ng"})
    public String add(String title) {
        return output(service.create(new Genre(title)), formatter::info);
    }

    @ShellMethod(value = "Edit genre", key = {"edit_genre", "eg"})
    public String edit(String id, String name) {
        return output(bookInfoService.renameGenre(new Genre(id, name)), formatter::editInfo);
    }

    @ShellMethod(value = "Remove genre", key = {"remove_genre", "rg"})
    public String remove(String id) {
        return output(bookInfoService.removeGenreById(id), removed -> formatter.removeInfo(id));
    }
}
