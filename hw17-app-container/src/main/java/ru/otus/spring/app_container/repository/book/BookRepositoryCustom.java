package ru.otus.spring.app_container.repository.book;


import ru.otus.spring.app_container.domain.Book;

import java.util.Optional;

public interface BookRepositoryCustom {
    Optional<Book> update(Book book);

    Optional<String> delete(String id);
}
