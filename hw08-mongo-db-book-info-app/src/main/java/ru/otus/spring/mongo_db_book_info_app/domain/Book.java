package ru.otus.spring.mongo_db_book_info_app.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.spring.mongo_db_book_info_app.dto.BookInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Document
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Book {
    @Id
    private String id;
    private String title;
    private List<Author> authors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();

    public Book(String title) {
        this.title = title;
    }

    public Book(String id, String title) {
        this(title);
        this.id = id;
    }

    public BookInfo toInfo() {
        return new BookInfo(this, Collections.emptyList());
    }

    public boolean isWrittenBy(Author author) {
        return
            authors
                .parallelStream()
                .anyMatch(
                    existingOne ->
                        existingOne.hasFirstAndLastName(author.getFirstName(), author.getLastName())
                )
            ;
    }

    public Book addAuthor(Author author) {
        return addToList(authors, author);
    }

    public Book addGenre(Genre genre) {
        return addToList(genres, genre);
    }

    public boolean hasGenre(Genre genre) {
        return genres.parallelStream().anyMatch(existingOne -> existingOne.hasName(genre.getName()));
    }

    public Book changeTitle(String title) {
        this.title = title;

        return this;
    }

    private<T> Book addToList(List<T> list, T newItem) {
        if (Objects.nonNull(newItem)) {
            list.add(newItem);
        }

        return this;
    }
}
