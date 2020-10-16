package ru.otus.spring.hw18.book_service.repository.book;


import ru.otus.spring.hw18.book_service.domain.Book;

import java.util.Optional;

public interface BookRepositoryCustom {
    Optional<Book> update(Book book);

    Optional<String> delete(String id);
}
