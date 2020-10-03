package ru.otus.spring.batch_migration.batch.migration.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@ToString
public class BookInfo {
    private final long id;
    private final String title;
    private final List<AuthorModel> authors;
    private final List<GenreModel> genres;
    private final List<CommentModel> comments;
}
