package ru.otus.spring.actuator.repository.book;


import ru.otus.spring.actuator.domain.Book;

import java.util.Optional;

public interface BookRepositoryCustom {
    Optional<Book> update(Book book);

    Optional<String> delete(String id);
}
