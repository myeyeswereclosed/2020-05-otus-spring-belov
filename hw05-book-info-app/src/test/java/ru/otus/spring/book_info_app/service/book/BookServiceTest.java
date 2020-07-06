package ru.otus.spring.book_info_app.service.book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.book_info_app.dao.book.BookDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.service.author.AuthorService;
import ru.otus.spring.book_info_app.service.genre.GenreService;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class BookServiceTest {
    private final static String BOOK_TITLE = "Tri porosenka";
    private final static long ID = 30L;

    private final static Genre GENRE = new Genre(24, "horror");
    private final static Author AUTHOR = new Author(11L, new Name("Some", "Author"));

    @Mock
    private BookDao dao;

    @Mock
    private AuthorService authorService;

    @Mock
    private GenreService genreService;

    @Test
    public void addBook() {
        when(dao.save(BOOK_TITLE)).thenReturn(new Book(ID, BOOK_TITLE));

        var service = new BookServiceImpl(dao);

        var result = service.addBook(BOOK_TITLE);

        assertSuccessfulResult(result);
    }

    @Test
    public void renameBook() {
        doAnswer(i -> {
            return null;
        }).when(dao).update(ID, BOOK_TITLE);

        var service = new BookServiceImpl(dao);

        var result = service.rename(ID, BOOK_TITLE);

        assertSuccessfulResult(result);
    }

    @Test
    public void removeBook() {
        doAnswer(i -> {
            return null;
        }).when(dao).delete(ID);

        var service = new BookServiceImpl(dao);

        var result = service.remove(ID);

        assertThat(result.isOk());
        assertThat(result.value().isEmpty());
    }

    @Test
    public void findBook() {
        when(dao.findById(ID)).thenReturn(new Book(ID, BOOK_TITLE));

        var service = new BookServiceImpl(dao);

        var result = service.find(ID);

        assertSuccessfulResult(result);
    }

    @Test
    public void findAll() {
        when(dao.findAll()).thenReturn(Collections.singletonList(new Book(ID, BOOK_TITLE)));

        var service = new BookServiceImpl(dao);

        var result = service.getAll();

        assertThat(result.isOk());
        assertThat(result.value().get().size()).isEqualTo(1);
        assertBook(result.value().get().get(0));
    }

    @Test
    public void addAuthor() {
        doAnswer(i -> {
            return null;
        }).when(dao).addAuthor(ID, AUTHOR);

        var service = new BookServiceImpl(dao);

        var result = service.addAuthor(ID, AUTHOR);

        assertThat(result.isOk());
        assertThat(result.value().isEmpty());
    }

    @Test
    public void addGenre() {
        doAnswer(i -> {
            return null;
        }).when(dao).addGenre(ID, GENRE);

        var service = new BookServiceImpl(dao);

        var result = service.addGenre(ID, GENRE);

        assertThat(result.isOk());
        assertThat(result.value().isEmpty());
    }

    private void assertSuccessfulResult(ServiceResult<Book> result) {
        assertThat(result.isOk());
        assertBook(result.value().get());
    }

    private void assertBook(Book book) {
        assertThat(book.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(book.getId()).isEqualTo(ID);
    }
}
