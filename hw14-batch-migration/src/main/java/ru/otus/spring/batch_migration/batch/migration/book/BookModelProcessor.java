package ru.otus.spring.batch_migration.batch.migration.book;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.otus.spring.batch_migration.batch.migration.model.*;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BookModelProcessor implements ItemProcessor<BookModel, BookInfo> {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public BookInfo process(BookModel book) throws Exception {
        var bookParamMap = Map.of("bookId", book.getId());

        var authors =
            jdbc.query(
                "select a.id, a.first_name, a.last_name from author a " +
                "join book_author ba on a.id = ba.author_id " +
                "where ba.book_id = :bookId",
                bookParamMap,
                new BeanPropertyRowMapper<>(AuthorModel.class)
            );

        var genres =
            jdbc.query(
                "select g.id, g.name from genre g " +
                "join book_genre bg on g.id = bg.genre_id " +
                "where bg.book_id = :bookId",
                bookParamMap,
                new BeanPropertyRowMapper<>(GenreModel.class)
            );

        var comments =
            jdbc.query(
                "select text from comment where book_id = :bookId",
                bookParamMap,
                new BeanPropertyRowMapper<>(CommentModel.class)
            );

        return new BookInfo(book.getId(), book.getTitle(), authors, genres, comments);
    }
}
