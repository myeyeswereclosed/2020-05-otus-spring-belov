package ru.otus.spring.book_info_app.service.shell.session;

import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddBookSession {
    private Book book;
    private List<Name> authors = new ArrayList<>();
    private List<String> genres = new ArrayList<>();

    public AddBookSession(Book book) {
        this.book = book;
    }

    public boolean canBeClosed() {
        return Objects.nonNull(book) && !authors.isEmpty() && !genres.isEmpty();
    }

    public void addAuthor(Name name) {
        authors.add(name);
    }

    public void addGenre(String name) {
        genres.add(name);
    }

    public Long bookId() {
        return book.getId();
    }
}
