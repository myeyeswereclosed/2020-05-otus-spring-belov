package ru.otus.spring.web_ui_book_info_app.repository.book;


import ru.otus.spring.web_ui_book_info_app.domain.Book;

import java.util.Optional;

public interface BookRepositoryCustom {
    Optional<Book> update(Book book);

    Optional<String> delete(String id);
}
