package ru.otus.spring.app_authentication.repository.book;


import org.springframework.stereotype.Repository;
import ru.otus.spring.app_authentication.domain.Book;

import java.util.Optional;


public interface BookRepositoryCustom {
    Optional<Book> update(Book book);

    Optional<String> delete(String id);
}
