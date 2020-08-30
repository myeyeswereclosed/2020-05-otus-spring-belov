package ru.otus.spring.reactive_book_info_app.repository.comment;

import lombok.Getter;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.reactive_book_info_app.domain.Author;
import ru.otus.spring.reactive_book_info_app.domain.Book;
import ru.otus.spring.reactive_book_info_app.domain.Genre;

@Getter
public class UpdateCommentConfig {
    private final String field;
    private final String value;
    private final Update update;

    private UpdateCommentConfig(String field, String value, Update update) {
        this.field = field;
        this.value = value;
        this.update = update;
    }

    public static UpdateCommentConfig renameBook(Book book) {
        return
            new UpdateCommentConfig(
                "book._id",
                book.getId(),
                new Update().set("book.title", book.getTitle())
            );
    }

    public static UpdateCommentConfig addAuthor(String bookId, Author author) {
        return
            new UpdateCommentConfig(
                "book.id",
                bookId,
                new Update().push("book.authors", author)
            );
    }

    public static UpdateCommentConfig addGenre(String bookId, Genre genre) {
        return
            new UpdateCommentConfig(
                "book.id",
                bookId,
                new Update().push("book.genres", genre)
            );
    }
}
