package ru.otus.spring.spring_data_jpa_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.spring_data_jpa_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.service.comment.CommentService;

import java.util.List;

@ShellComponent
public class CommentCommandsExecutor extends BaseCommandExecutor {
    private final CommentService service;

    public CommentCommandsExecutor(CommentService service, ShellOutputConfig config) {
        super(config);
        this.service = service;
    }

    @ShellMethod(value = "Edit comment", key = {"edit_comment", "ec"})
    public String edit(int id, String text) {
        return output(service.edit(new Comment(id, text)));
    }

    @ShellMethod(value = "Get comment", key = {"get_comment", "gc"})
    public String get(long id) {
        return output(service.find(id), Comment::toString);
    }

    @ShellMethod(value = "Get all comments", key = {"get_all_comments", "gac"})
    public String getAll() {
        return output(service.findAll(), List::toString);
    }

    @ShellMethod(value = "Remove comment", key = {"remove_comment", "rc"})
    public String remove(long id) {
        return output(service.remove(id), "Comment deleted");
    }
}
