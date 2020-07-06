package ru.otus.spring.book_info_app.service.book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.service.author.AuthorService;
import ru.otus.spring.book_info_app.service.book_info.BookInfoServiceImpl;
import ru.otus.spring.book_info_app.service.genre.GenreService;
import ru.otus.spring.book_info_app.service.result.FailResult;
import ru.otus.spring.book_info_app.service.result.SuccessResult;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class BookInfoServiceTest {
    private final static Book BOOK = new Book(30L, "Tri porosenka");
    private final static Genre GENRE = new Genre(24, "horror");
    private final static Author AUTHOR = new Author(11L, new Name("Some", "Author"));

    @Mock
    private BookService bookService;

    @Mock
    private AuthorService authorService;

    @Mock
    private GenreService genreService;

    @Test
    public void addExistingGenre() {
        when(genreService.getByName(GENRE.getName())).thenReturn(new SuccessResult<>(GENRE));
        when(bookService.addGenre(BOOK.getId(), GENRE)).thenReturn(SuccessResult.unit());

        var service = new BookInfoServiceImpl(bookService, authorService, genreService);

        var result = service.addGenre(BOOK.getId(), GENRE.getName());

        assertThat(result.isOk());
        verify(bookService, times(1)).addGenre(BOOK.getId(), GENRE);
        verify(genreService, times(1)).getByName(GENRE.getName());
        verify(genreService, never()).create(GENRE.getName());
    }

    @Test
    public void addNewGenre() {
        when(genreService.getByName(GENRE.getName())).thenReturn(new FailResult<>());
        when(genreService.create(GENRE.getName())).thenReturn(new SuccessResult<>(GENRE));

        var service = new BookInfoServiceImpl(bookService, authorService, genreService);

        var result = service.addGenre(BOOK.getId(), GENRE.getName());

        assertThat(result.isOk());
        verify(bookService, times(1)).addGenre(BOOK.getId(), GENRE);
        verify(genreService, times(1)).getByName(GENRE.getName());
        verify(genreService, times(1)).create(GENRE.getName());
    }

    @Test
    public void addExistingAuthor() {
        when(authorService.getByName(AUTHOR.getName())).thenReturn(new SuccessResult<>(AUTHOR));
        when(bookService.addAuthor(BOOK.getId(), AUTHOR)).thenReturn(SuccessResult.unit());

        var service = new BookInfoServiceImpl(bookService, authorService, genreService);

        var result = service.addAuthor(BOOK.getId(), AUTHOR.getName());

        assertThat(result.isOk());
        verify(bookService, times(1)).addAuthor(BOOK.getId(), AUTHOR);
        verify(authorService, times(1)).getByName(AUTHOR.getName());
        verify(authorService, never()).create(AUTHOR.getName());
    }

    @Test
    public void addNewAuthor() {
        when(authorService.getByName(AUTHOR.getName())).thenReturn(new FailResult<>());
        when(authorService.create(AUTHOR.getName())).thenReturn(new SuccessResult<>(AUTHOR));

        var service = new BookInfoServiceImpl(bookService, authorService, genreService);

        var result = service.addAuthor(BOOK.getId(), AUTHOR.getName());

        assertThat(result.isOk());
        verify(bookService, times(1)).addAuthor(BOOK.getId(), AUTHOR);
        verify(authorService, times(1)).getByName(AUTHOR.getName());
        verify(authorService, times(1)).create(AUTHOR.getName());
    }

    @Test
    public void getInfo() {
        when(bookService.find(BOOK.getId())).thenReturn(new SuccessResult<>(BOOK));
        when(authorService.getByBook(BOOK)).thenReturn(new SuccessResult<>(singletonList(AUTHOR)));
        when(genreService.getByBook(BOOK)).thenReturn(new SuccessResult<>(singletonList(GENRE)));

        var service = new BookInfoServiceImpl(bookService, authorService, genreService);

        var result = service.get(BOOK.getId());

        assertThat(result.isOk());

        var bookInfo = result.value().get();

        assertThat(bookInfo.getBook()).isEqualTo(BOOK);
        assertThat(bookInfo.getAuthors()).isEqualTo(singletonList(AUTHOR));
        assertThat(bookInfo.getGenres()).isEqualTo(singletonList(GENRE));
    }
}
