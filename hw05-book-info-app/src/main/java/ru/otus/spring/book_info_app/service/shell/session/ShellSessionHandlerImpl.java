package ru.otus.spring.book_info_app.service.shell.session;

import org.springframework.shell.Availability;
import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.config.CommandAvailabilityConfig;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.service.book.BookService;
import ru.otus.spring.book_info_app.service.book_info.BookInfoService;
import ru.otus.spring.book_info_app.service.result.FailResult;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.Objects;
import java.util.Optional;

@Service
public class ShellSessionHandlerImpl implements ShellSessionHandler {
    private AddBookSession currentSession;
    private final BookService bookService;
    private final BookInfoService bookInfoService;
    private final CommandAvailabilityConfig commandAvailabilityConfig;

    public ShellSessionHandlerImpl(
        BookService bookService,
        BookInfoService bookInfoService,
        CommandAvailabilityConfig commandAvailabilityConfig
    ) {
        this.bookService = bookService;
        this.bookInfoService = bookInfoService;
        this.commandAvailabilityConfig = commandAvailabilityConfig;
    }

    @Override
    public ServiceResult<Void> removeBook(long id) {
        var serviceResult = bookService.remove(id);

        session().ifPresent(
            session -> {
                if (session.bookId() == id && serviceResult.isOk()) {
                    this.currentSession = null;
                }
            }
        );

        return serviceResult;
    }

    @Override
    public ServiceResult<Void> addBookAuthor(Name name) {
        return
            session()
                .map(session -> {
                    var serviceResult = bookInfoService.addAuthor(session.bookId(), name);

                    if (serviceResult.isOk()) {
                        session.addAuthor(name);
                    }

                    return serviceResult;
                })
                .orElseGet(FailResult::new)
        ;
    }

    @Override
    public ServiceResult<Void> addBookGenre(String name) {
        return
            session()
                .map(session -> {
                    var result = bookInfoService.addGenre(session.bookId(), name);

                    if (result.isOk()) {
                        session.addGenre(name);
                    }

                    return result;
                })
                .orElseGet(FailResult::new)
            ;
    }

    @Override
    public Optional<AddBookSession> session() {
        return Optional.ofNullable(currentSession);
    }

    @Override
    public ServiceResult<Book> startSession(String bookTitle) {
        var result = bookService.addBook(bookTitle);

        result.value().map(book -> this.currentSession = new AddBookSession(book));

        return result;
    }

    @Override
    public Availability canStartSession() {
        return
            Objects.isNull(currentSession) || currentSession.canBeClosed()
                ? Availability.available()
                : Availability.unavailable(commandAvailabilityConfig.getSessionNotFinishedMessage())
        ;
    }

    @Override
    public Availability canAddBookInfo() {
        return
            Objects.nonNull(currentSession)
                ? Availability.available()
                : Availability.unavailable(commandAvailabilityConfig.getNoCurrentBookMessage())
        ;
    }
}
