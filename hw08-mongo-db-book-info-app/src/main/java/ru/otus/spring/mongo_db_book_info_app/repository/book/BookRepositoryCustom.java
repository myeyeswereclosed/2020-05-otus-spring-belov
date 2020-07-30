package ru.otus.spring.mongo_db_book_info_app.repository.book;

import ru.otus.spring.mongo_db_book_info_app.domain.Book;

import java.util.Optional;

public interface BookRepositoryCustom {
    Optional<Book> update(Book book);

    Optional<String> delete(String id);
}
