package ru.otus.spring.app_authorization.repository.book;


import ru.otus.spring.app_authorization.domain.Book;

import java.util.Optional;


public interface BookRepositoryCustom {
    Optional<Book> update(Book book);

    Optional<String> delete(String id);
}
