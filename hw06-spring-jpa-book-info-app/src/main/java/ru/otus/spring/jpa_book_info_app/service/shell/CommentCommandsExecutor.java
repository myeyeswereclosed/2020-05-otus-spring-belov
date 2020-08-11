package ru.otus.spring.jpa_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.jpa_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.jpa_book_info_app.domain.Comment;
import ru.otus.spring.jpa_book_info_app.service.comment.CommentService;
import ru.otus.spring.jpa_book_info_app.service.shell.formatter.OutputFormatter;

import java.util.List;

@ShellComponent
public class CommentCommandsExecutor extends BaseCommandExecutor {
    private final CommentService service;
    private final OutputFormatter<Comment> commentOutputFormatter;
    private final OutputFormatter<List<Comment>> commentsOutputFormatter;

    public CommentCommandsExecutor(CommentService service, ShellOutputConfig config, OutputFormatter<Comment> commentOutputFormatter, OutputFormatter<List<Comment>> commentsOutputFormatter) {
        super(config);
        this.service = service;
        this.commentOutputFormatter = commentOutputFormatter;
        this.commentsOutputFormatter = commentsOutputFormatter;
    }

    @ShellMethod(value = "Edit comment", key = {"edit_comment", "ec"})
    public String edit(int id, String text) {
        return output(service.edit(new Comment(id, text)));
    }

    @ShellMethod(value = "Get comment", key = {"get_comment", "gc"})
    public String get(long id) {
        return output(service.find(id), commentOutputFormatter::format);
    }

    @ShellMethod(value = "Get all comments", key = {"get_all_comments", "gac"})
    public String getAll() {
        return output(service.findAll(), commentsOutputFormatter::format);
    }

    @ShellMethod(value = "Remove comment", key = {"remove_comment", "rc"})
    public String remove(long id) {
        return output(service.remove(id), "Comment deleted");
    }
}
