package ru.otus.spring.book_info_app.service.shell.session;

import org.springframework.shell.Availability;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.Optional;

public interface ShellSessionHandler {
    Optional<AddBookSession> session();

    ServiceResult<Book> startSession(String bookTitle);

    Availability canStartSession();

    Availability canAddBookInfo();

    ServiceResult<Void> addBookGenre(String name);

    ServiceResult<Void> addBookAuthor(Name name);

    ServiceResult<Void> removeBook(long id);
}
