package ru.otus.spring.jpa_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.spring.jpa_book_info_app.domain.Author;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.domain.Comment;
import ru.otus.spring.jpa_book_info_app.domain.Genre;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public class BookInfo {
    private long id;
    private String title;
    private Set<Author> authors;
    private Set<Genre> genres;
    private Set<Comment> comments;

    public BookInfo(Book book, Set<Comment> comments) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authors = new HashSet<>(book.getAuthors());
        this.genres = new HashSet<>(book.getGenres());
        this.comments = comments;
    }
//
//    public BookInfo(Book book) {
//        this(book, Collections.emptySet());
//    }

    public long bookId() {
        return id;
    }
}
