package ru.otus.spring.reactive_book_info_app.controller.rest.handler;

import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.reactive_book_info_app.controller.rest.handler.book_info.BookInfoHandler;
import ru.otus.spring.reactive_book_info_app.domain.Author;
import ru.otus.spring.reactive_book_info_app.domain.Book;
import ru.otus.spring.reactive_book_info_app.domain.Comment;
import ru.otus.spring.reactive_book_info_app.domain.Genre;
import ru.otus.spring.reactive_book_info_app.repository.author.AuthorRepository;
import ru.otus.spring.reactive_book_info_app.repository.book.BookRepository;
import ru.otus.spring.reactive_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.reactive_book_info_app.repository.comment.UpdateCommentConfig;
import ru.otus.spring.reactive_book_info_app.repository.genre.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Обработчик запросов сводной информации о книгах должен ")
@SpringBootTest
public class BookInfoHandlerTest {
    private static final Exception EXCEPTION = new Exception("Stub Exception");

    private static final String BOOK_TITLE = "Tri porosenka";
    private static final String COMMENT_TEXT = "Good book!";

    private static final Author AUTHOR = new Author("Some", "Author");
    private static final Author NEW_AUTHOR = new Author("Another", "Author");

    private static final Genre GENRE = new Genre("horror");
    private static final Genre NEW_GENRE = new Genre("love-story");

    private static final Book BOOK = new Book("StubId", BOOK_TITLE, List.of(AUTHOR), List.of(GENRE));
    private static final Comment COMMENT = new Comment(COMMENT_TEXT, BOOK);

    @Autowired
    private BookInfoHandler handler;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @DisplayName("находить информацию о сохраненной книге")
    @Test
    public void getBook() {
        when(bookRepository.findById(BOOK.getId())).thenReturn(Mono.just(BOOK));
        when(commentRepository.findAllByBook_Id(BOOK.getId())).thenReturn(Flux.just(COMMENT));

        StepVerifier
            .create(handler.getInfo(BOOK.getId()))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать ошибку клиента при попытке найти информацию о несохраненной книге")
    @Test
    public void getNonStoredBook() {
        when(bookRepository.findById(BOOK.getId())).thenReturn(Mono.empty());

        StepVerifier
            .create(handler.getInfo(BOOK.getId()))
            .expectNextMatches(response -> response.statusCode().is4xxClientError())
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать ошибку сервера если она возникла при поиске комментариев к книге")
    @Test
    public void getStoredBookWithError() {
        when(bookRepository.findById(BOOK.getId())).thenReturn(Mono.just(BOOK));
        when(commentRepository.findAllByBook_Id(BOOK.getId())).thenReturn(Flux.error(EXCEPTION));

        StepVerifier
            .create(handler.getInfo(BOOK.getId()))
            .expectNextMatches(response -> response.statusCode().is5xxServerError())
            .verifyComplete()
        ;
    }

    @DisplayName("игнорировать добавление уже существующего автора книги")
    @Test
    public void ignoreAlreadyKnownBookAuthor() {
        when(bookRepository.findById(BOOK.getId())).thenReturn(Mono.just(BOOK));

        StepVerifier
            .create(handler.addAuthor(BOOK.getId(), AUTHOR))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;

        verify(authorRepository, never()).findByFirstNameAndLastName(AUTHOR.getFirstName(), AUTHOR.getLastName());
        verify(authorRepository, never()).save(AUTHOR);
    }

    @DisplayName("добавлять нового автора книги")
    @Test
    public void addNewAuthor() {
        var book = new Book("StubId", BOOK_TITLE, new ArrayList<>(){{ add(AUTHOR); }}, List.of(GENRE));

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        when(authorRepository.findByFirstNameAndLastName(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName()))
            .thenReturn(Mono.empty());
        when(authorRepository.save(NEW_AUTHOR)).thenReturn(Mono.just(NEW_AUTHOR));

        when(commentRepository.update(any(UpdateCommentConfig.class)))
            .thenReturn(Mono.just(UpdateResult.acknowledged(1, 1L, null)));

        assertThat(book.isWrittenBy(NEW_AUTHOR)).isFalse();

        StepVerifier
            .create(handler.addAuthor(book.getId(), NEW_AUTHOR))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;

        assertThat(book.isWrittenBy(NEW_AUTHOR)).isTrue();

        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookRepository, times(1)).save(book);
        verify(authorRepository, times(1))
            .findByFirstNameAndLastName(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName());
        verify(authorRepository, times(1)).save(NEW_AUTHOR);
        verify(commentRepository).update(any(UpdateCommentConfig.class));
    }

    @DisplayName("добавлять нового автора книги")
    @Test
    public void addStoredAuthor() {
        var book = new Book("StubId", BOOK_TITLE, new ArrayList<>(){{ add(AUTHOR); }}, List.of(GENRE));

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        when(authorRepository.findByFirstNameAndLastName(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName()))
            .thenReturn(Mono.just(NEW_AUTHOR));

        when(commentRepository.update(any(UpdateCommentConfig.class)))
            .thenReturn(Mono.just(UpdateResult.acknowledged(1, 1L, null)));

        assertThat(book.isWrittenBy(NEW_AUTHOR)).isFalse();

        StepVerifier
            .create(handler.addAuthor(book.getId(), NEW_AUTHOR))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;

        assertThat(book.isWrittenBy(NEW_AUTHOR)).isTrue();

        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookRepository, times(1)).save(book);
        verify(authorRepository, times(1))
            .findByFirstNameAndLastName(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName());
        verify(authorRepository, never()).save(NEW_AUTHOR);
        verify(commentRepository).update(any(UpdateCommentConfig.class));
    }

    @DisplayName("отдавать ошибку клиента при попытке добавить автора несохраненной книги")
    @Test
    public void addAuthorOfNonStoredBook() {
        when(bookRepository.findById(BOOK.getId())).thenReturn(Mono.empty());

        StepVerifier
            .create(handler.addAuthor(BOOK.getId(), NEW_AUTHOR))
            .expectNextMatches(response -> response.statusCode().is4xxClientError())
            .verifyComplete()
        ;
    }

    @DisplayName("игнорировать добавление уже существующего жанра книги")
    @Test
    public void ignoreAlreadyKnownBookGenre() {
        when(bookRepository.findById(BOOK.getId())).thenReturn(Mono.just(BOOK));

        StepVerifier
            .create(handler.addGenre(BOOK.getId(), GENRE))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;

        verify(genreRepository, never()).findByName(GENRE.getName());
        verify(genreRepository, never()).save(GENRE);
    }

    @DisplayName("добавлять новый жанр книги")
    @Test
    public void addNewGenre() {
        var book = new Book("StubId", BOOK_TITLE, List.of(AUTHOR), new ArrayList<>() {{ add(GENRE); }});

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        when(genreRepository.findByName(NEW_GENRE.getName())).thenReturn(Mono.empty());
        when(genreRepository.save(NEW_GENRE)).thenReturn(Mono.just(NEW_GENRE));

        when(commentRepository.update(any(UpdateCommentConfig.class)))
            .thenReturn(Mono.just(UpdateResult.acknowledged(1, 1L, null)));

        assertThat(book.hasGenre(NEW_GENRE)).isFalse();

        StepVerifier
            .create(handler.addGenre(book.getId(), NEW_GENRE))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;

        assertThat(book.hasGenre(NEW_GENRE)).isTrue();

        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookRepository, times(1)).save(book);
        verify(genreRepository, times(1)).findByName(NEW_GENRE.getName());
        verify(genreRepository, times(1)).save(NEW_GENRE);
        verify(commentRepository).update(any(UpdateCommentConfig.class));
    }

    @DisplayName("добавлять существующий жанр книги")
    @Test
    public void addStoredGenre() {
        var book = new Book("StubId", BOOK_TITLE, List.of(AUTHOR), new ArrayList<>() {{ add(GENRE); }});

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        when(genreRepository.findByName(NEW_GENRE.getName())).thenReturn(Mono.just(NEW_GENRE));

        when(commentRepository.update(any(UpdateCommentConfig.class)))
            .thenReturn(Mono.just(UpdateResult.acknowledged(1, 1L, null)));

        assertThat(book.hasGenre(NEW_GENRE)).isFalse();

        StepVerifier
            .create(handler.addGenre(book.getId(), NEW_GENRE))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;

        assertThat(book.hasGenre(NEW_GENRE)).isTrue();

        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookRepository, times(1)).save(book);
        verify(genreRepository, times(1)).findByName(NEW_GENRE.getName());
        verify(genreRepository, never()).save(NEW_GENRE);
        verify(commentRepository).update(any(UpdateCommentConfig.class));
    }

    @DisplayName("добавлять комментарий к книге")
    @Test
    public void addComment() {
        var book = new Book("StubId", BOOK_TITLE, List.of(AUTHOR), List.of(GENRE));
        var comment = new Comment(COMMENT_TEXT);

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(commentRepository.save(comment)).thenReturn(Mono.just(comment));

        assertThat(comment.getBook()).isNull();

        StepVerifier
            .create(handler.addComment(book.getId(), comment))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;

        assertThat(comment.getBook()).isEqualTo(book);

        verify(bookRepository, times(1)).findById(book.getId());
        verify(commentRepository, times(1)).save(comment);
    }

    @DisplayName("отдавать ошибку клиента при попытке добавить комментарий к несохраненной книге")
    @Test
    public void addCommentToNonStoredBook() {
        var book = new Book("StubId", BOOK_TITLE, List.of(AUTHOR), List.of(GENRE));
        var comment = new Comment(COMMENT_TEXT);

        when(bookRepository.findById(book.getId())).thenReturn(Mono.empty());

        StepVerifier
            .create(handler.addComment(book.getId(), comment))
            .expectNextMatches(response -> response.statusCode().is4xxClientError())
            .verifyComplete()
        ;

        verify(bookRepository, times(1)).findById(book.getId());
        verify(commentRepository, never()).save(comment);
    }
}
