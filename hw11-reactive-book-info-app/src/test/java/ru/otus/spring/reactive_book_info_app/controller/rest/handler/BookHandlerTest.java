package ru.otus.spring.reactive_book_info_app.controller.rest.handler;

import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.reactive_book_info_app.controller.rest.handler.book.BookHandler;
import ru.otus.spring.reactive_book_info_app.domain.Book;
import ru.otus.spring.reactive_book_info_app.repository.book.BookRepository;
import ru.otus.spring.reactive_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.reactive_book_info_app.repository.comment.UpdateCommentConfig;

@DisplayName("Обработчик запросов по книгам должен ")
@SpringBootTest
public class BookHandlerTest {
    private static final Book BOOK = new Book("StubId", "Tri porosenka");

    @Autowired
    private BookHandler handler;

    @MockBean
    private BookRepository repository;

    @MockBean
    private CommentRepository commentRepository;

    @DisplayName("добавлять новую книгу")
    @Test
    public void addBook() {
        Mockito.when(repository.save(BOOK)).thenReturn(Mono.just(BOOK));

        StepVerifier
            .create(handler.addBook(BOOK))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;
    }

    @DisplayName("формировать успешный ответ при запросе всех книг")
    @Test
    public void getAll() {
        Mockito.when(repository.findAll()).thenReturn(Flux.just(BOOK));

        StepVerifier
            .create(handler.getAll())
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;
    }

    @DisplayName("формировать ошибку сервера при ее возникновении при запросе всех книг")
    @Test
    public void getAllWithError() {
        Mockito.when(repository.findAll()).thenReturn(Flux.error(new Exception()));

        StepVerifier
            .create(handler.getAll())
            .expectNextMatches(response -> response.statusCode().is5xxServerError())
            .verifyComplete()
        ;
    }

    @DisplayName("формировать ошибочный ответ при возникновении исключений на уровне репозитория")
    @Test
    public void addBookWithError() {
        Mockito.when(repository.save(BOOK)).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier
            .create(handler.addBook(BOOK))
            .expectNextMatches(response -> response.statusCode().is5xxServerError())
            .verifyComplete()
        ;
    }

    @DisplayName("переименовывать хранимую книгу")
    @Test
    public void renameBook() {
        Mockito.when(repository.update(BOOK)).thenReturn(Mono.just(BOOK));
        Mockito
            .when(commentRepository.update(Mockito.any(UpdateCommentConfig.class)))
            .thenReturn(Mono.just(UpdateResult.acknowledged(1, 1L, null)))
        ;

        StepVerifier
            .create(handler.rename(BOOK))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать ошибку клиента при попытке переименовать несохраненную книгу")
    @Test
    public void renameNonStoredBook() {
        Mockito.when(repository.update(BOOK)).thenReturn(Mono.empty());

        StepVerifier
            .create(handler.rename(BOOK))
            .expectNextMatches(response -> response.statusCode().is4xxClientError())
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать ошибку сервера при ее возникновении в репозитории книг в процессе редактирования")
    @Test
    public void renameWithError() {
        Mockito.when(repository.update(BOOK)).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier
            .create(handler.rename(BOOK))
            .expectNextMatches(response -> response.statusCode().is5xxServerError())
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать ошибку сервера при ее возникновении в репозитории комментариев в процессе редактирования")
    @Test
    public void renameWithCommentsError() {
        Mockito.when(repository.update(BOOK)).thenReturn(Mono.just(BOOK));
        Mockito
            .when(commentRepository.update(UpdateCommentConfig.renameBook(BOOK)))
            .thenReturn(Mono.error(new RuntimeException()))
        ;

        StepVerifier
            .create(handler.rename(BOOK))
            .expectNextMatches(response -> response.statusCode().is5xxServerError())
            .verifyComplete()
        ;
    }

    @DisplayName("удалять хранимую книгу")
    @Test
    public void removeBook() {
        Mockito.when(repository.delete(BOOK.getId())).thenReturn(Mono.just(BOOK.getId()));
        Mockito
            .when(commentRepository.deleteAllByBook_Id(BOOK.getId()))
            .thenReturn(Mono.just(1L))
        ;

        StepVerifier
            .create(handler.remove(BOOK.getId()))
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать ошибку клиента при попытке удалить несохраненную книгу")
    @Test
    public void removeNonStoredBook() {
        Mockito.when(repository.delete(BOOK.getId())).thenReturn(Mono.empty());

        StepVerifier
            .create(handler.remove(BOOK.getId()))
            .expectNextMatches(response -> response.statusCode().is4xxClientError())
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать ошибку сервера при ее возникновении в репозитории книг в процессе удаления")
    @Test
    public void removeWithError() {
        Mockito.when(repository.delete(BOOK.getId())).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier
            .create(handler.remove(BOOK.getId()))
            .expectNextMatches(response -> response.statusCode().is5xxServerError())
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать ошибку сервера при ее возникновении в репозитории комментариев в процессе удаления")
    @Test
    public void removeWithCommentsError() {
        Mockito.when(repository.delete(BOOK.getId())).thenReturn(Mono.just(BOOK.getId()));
        Mockito
            .when(commentRepository.deleteAllByBook_Id(BOOK.getId()))
            .thenReturn(Mono.error(new RuntimeException()))
        ;

        StepVerifier
            .create(handler.remove(BOOK.getId()))
            .expectNextMatches(response -> response.statusCode().is5xxServerError())
            .verifyComplete()
        ;
    }
}
