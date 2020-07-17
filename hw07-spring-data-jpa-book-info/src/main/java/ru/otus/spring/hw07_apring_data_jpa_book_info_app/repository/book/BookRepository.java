package ru.otus.spring.hw07_apring_data_jpa_book_info_app.repository.book;

import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(long id);

    boolean delete(long id);

    List<Book> findAll();
}
