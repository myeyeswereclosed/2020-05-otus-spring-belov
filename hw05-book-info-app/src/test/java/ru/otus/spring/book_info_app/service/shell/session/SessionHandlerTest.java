package ru.otus.spring.book_info_app.service.shell.session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.book_info_app.config.CommandAvailabilityConfig;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.service.book.BookService;
import ru.otus.spring.book_info_app.service.book_info.BookInfoService;
import ru.otus.spring.book_info_app.service.result.SuccessResult;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SessionHandlerTest {
    private static final Book BOOK = new Book(1L, "Tri porosenka");
    private static final Book ANOTHER_BOOK = new Book(10L, "Tri kotenka");
    private static final Author AUTHOR = new Author(2L, new Name("Some", "Author"));
    private static final Genre GENRE = new Genre(3, "horror");

    @Mock
    private BookService bookService;

    @Mock
    private BookInfoService bookInfoService;

    @Mock
    private CommandAvailabilityConfig commandAvailabilityConfig;

    @Test
    public void startSession() {
        when(bookService.addBook(BOOK.getTitle())).thenReturn(new SuccessResult<>(BOOK));
        when(commandAvailabilityConfig.getNoCurrentBookMessage()).thenReturn("Stub message");

        var handler = new ShellSessionHandlerImpl(bookService, bookInfoService, commandAvailabilityConfig);

        assertThat(!handler.canAddBookInfo().isAvailable());
        assertThat(handler.canStartSession().isAvailable());

        var result = handler.startSession(BOOK.getTitle());

        assertThat(result.isOk());
        assertSessionBook(handler.session().get());
    }

    @Test
    public void newSessionIsUnavailableIfNotAllBookInfoProvided() {
        when(bookService.addBook(BOOK.getTitle())).thenReturn(new SuccessResult<>(BOOK));
        when(bookInfoService.addAuthor(BOOK.getId(), AUTHOR.getName())).thenReturn(SuccessResult.unit());
        when(commandAvailabilityConfig.getSessionNotFinishedMessage()).thenReturn("Stub message");

        var handler = new ShellSessionHandlerImpl(bookService, bookInfoService, commandAvailabilityConfig);

        handler.startSession(BOOK.getTitle());

        assertThat(!handler.canStartSession().isAvailable());

        handler.addBookAuthor(AUTHOR.getName());

        assertThat(!handler.canStartSession().isAvailable());
    }

    @Test
    public void addAuthor() {
        when(bookService.addBook(BOOK.getTitle())).thenReturn(new SuccessResult<>(BOOK));
        when(bookInfoService.addAuthor(BOOK.getId(), AUTHOR.getName())).thenReturn(SuccessResult.unit());

        var handler = new ShellSessionHandlerImpl(bookService, bookInfoService, commandAvailabilityConfig);
        handler.startSession(BOOK.getTitle());

        var result = handler.addBookAuthor(AUTHOR.getName());

        assertThat(result.isOk());

        var session = handler.session().get();
        assertSessionBook(session);
        assertThat(session).hasFieldOrPropertyWithValue("authors", Collections.singletonList(AUTHOR.getName()));
        assertThat(session).hasFieldOrPropertyWithValue("genres", Collections.emptyList());
        assertThat(!session.canBeClosed());
    }

    @Test
    public void addGenre() {
        when(bookService.addBook(BOOK.getTitle())).thenReturn(new SuccessResult<>(BOOK));
        when(bookInfoService.addGenre(BOOK.getId(), GENRE.getName())).thenReturn(SuccessResult.unit());

        var handler = new ShellSessionHandlerImpl(bookService, bookInfoService, commandAvailabilityConfig);
        handler.startSession(BOOK.getTitle());

        var result = handler.addBookGenre(GENRE.getName());

        assertThat(result.isOk());

        var session = handler.session().get();
        assertSessionBook(session);
        assertThat(session).hasFieldOrPropertyWithValue("genres", Collections.singletonList(GENRE.getName()));
        assertThat(session).hasFieldOrPropertyWithValue("authors", Collections.emptyList());
        assertThat(!session.canBeClosed());
    }

    @Test
    public void sessionCanBeClosedIfAllBookDataProvided() {
        when(bookService.addBook(BOOK.getTitle())).thenReturn(new SuccessResult<>(BOOK));
        when(bookInfoService.addAuthor(BOOK.getId(), AUTHOR.getName())).thenReturn(SuccessResult.unit());
        when(bookInfoService.addGenre(BOOK.getId(), GENRE.getName())).thenReturn(SuccessResult.unit());

        var handler = new ShellSessionHandlerImpl(bookService, bookInfoService, commandAvailabilityConfig);
        handler.startSession(BOOK.getTitle());
        handler.addBookAuthor(AUTHOR.getName());
        handler.addBookGenre(GENRE.getName());

        assertThat(handler.session().get().canBeClosed());
    }

    @Test
    public void removeBookFromSession() {
        when(bookService.addBook(BOOK.getTitle())).thenReturn(new SuccessResult<>(BOOK));
        when(bookService.remove(BOOK.getId())).thenReturn(SuccessResult.unit());

        var handler = new ShellSessionHandlerImpl(bookService, bookInfoService, commandAvailabilityConfig);
        handler.startSession(BOOK.getTitle());

        handler.removeBook(BOOK.getId());

        assertThat(handler.session().isEmpty());
    }

    @Test
    public void doNothingIfSessionIsEmptyAndSomeBookShouldBeRemoved() {
        when(bookService.remove(BOOK.getId())).thenReturn(SuccessResult.unit());

        var handler = new ShellSessionHandlerImpl(bookService, bookInfoService, commandAvailabilityConfig);

        handler.removeBook(BOOK.getId());

        assertThat(handler.session().isEmpty());
    }

    @Test
    public void doNotRemoveBookFromSessionIfBookWithAnotherIdShouldBeRemoved() {
        when(bookService.addBook(ANOTHER_BOOK.getTitle())).thenReturn(new SuccessResult<>(ANOTHER_BOOK));
        when(bookService.remove(BOOK.getId())).thenReturn(SuccessResult.unit());

        var handler = new ShellSessionHandlerImpl(bookService, bookInfoService, commandAvailabilityConfig);
        handler.startSession(ANOTHER_BOOK.getTitle());

        handler.removeBook(BOOK.getId());

        assertThat(handler.session().get().bookId()).isEqualTo(ANOTHER_BOOK.getId());
    }

    private void assertSessionBook(AddBookSession session) {
        assertThat(session.bookId()).isEqualTo(BOOK.getId());
        assertThat(!session.canBeClosed());
    }
}
