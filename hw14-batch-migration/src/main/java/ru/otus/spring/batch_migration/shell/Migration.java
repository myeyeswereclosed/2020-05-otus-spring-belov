package ru.otus.spring.batch_migration.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.batch_migration.batch.migration.BookInfoMigration;
import ru.otus.spring.batch_migration.config.JobConfig;

import java.time.LocalDateTime;

@ShellComponent
@RequiredArgsConstructor
public class Migration {
    private final JobOperator operator;
    private final BookInfoMigration migration;

    @ShellMethod(value = "startMigration", key = {"m", "migrate"})
    public void migrate() throws Exception {
        operator.start(migration.name(), LocalDateTime.now().toString());
    }
}
