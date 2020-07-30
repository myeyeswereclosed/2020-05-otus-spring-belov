package ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.genre;

import org.springframework.stereotype.Service;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.OperationResultFormatter;

@Service
public class GenreOperationFormatter implements OperationResultFormatter<Genre> {
    @Override
    public String editInfo(Genre edited) {
        return new StringBuilder("Genre edited as '").append(edited.getName()).append('\'').toString();
    }

    @Override
    public String removeInfo(String id) {
        return new StringBuilder("Genre with id = ").append(id).append(" removed").toString();
    }

    @Override
    public String info(Genre genre) {
        return new StringBuilder("id: ").append(genre.getId()).append(", ").append(genre.getName()).toString();
    }
}
