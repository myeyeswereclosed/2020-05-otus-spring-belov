package ru.otus.spring.app_container.repository.comment;

import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.app_container.domain.Author;
import ru.otus.spring.app_container.domain.Book;
import ru.otus.spring.app_container.domain.Genre;

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

    public static UpdateCommentConfig updateAuthor(Author author) {
        return
            new UpdateCommentConfig(
                "book.authors.id",
                author.getId(),
                new Update()
                    .set("book.authors.$.firstName", author.getFirstName())
                    .set("book.authors.$.lastName", author.getLastName())
            );
    }

    public static UpdateCommentConfig removeAuthor(String authorId) {
        var query = new Query(Criteria.where("id").is(authorId));

        return
            new UpdateCommentConfig(
                "book.authors.id",
                authorId,
                new Update().pull("book.authors", query)
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

    public static UpdateCommentConfig updateGenre(Genre genre) {
        return
            new UpdateCommentConfig(
                "book.genres.id",
                genre.getId(),
                new Update().set("book.genres.$.name", genre.getName())
            );
    }

    public static UpdateCommentConfig removeGenre(String genreId) {
        var query = new Query(Criteria.where("id").is(genreId));

        return
            new UpdateCommentConfig(
                "book.genres.id",
                genreId,
                new Update().pull("book.genres", query)
            );
    }
}
