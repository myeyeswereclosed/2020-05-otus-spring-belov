package ru.otus.spring.book_info_app.service.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import ru.otus.spring.book_info_app.dao.author.AuthorDao;
import ru.otus.spring.book_info_app.dao.book.BookDao;
import ru.otus.spring.book_info_app.dao.genre.GenreDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Сервис сводной информации о книгах должен ")
@SpringBootTest
public class BookInfoServiceTest {
    private final static Book BOOK = new Book(30L, "Tri porosenka");
    private final static Genre GENRE = new Genre(24, "horror");
    private final static Author AUTHOR = new Author(11L, "Test", "Author");

    @MockBean
    private BookDao bookDao;

    @MockBean
    private AuthorDao authorDao;

    @MockBean
    private GenreDao genreDao;

    @Autowired
    BookInfoServiceImpl service;

    @Test
    @DisplayName("добавлять книге ранее сохраненный жанр")
    public void addExistingGenre() {
        when(genreDao.findByName(GENRE.getName())).thenReturn(GENRE);

        var result = service.addBookGenre(BOOK.getId(), GENRE);

        assertThat(result.isOk());
        verify(bookDao, times(1)).addGenre(BOOK.getId(), GENRE);
        verify(genreDao, times(1)).findByName(GENRE.getName());
        verify(genreDao, never()).save(GENRE);
    }

    @Test
    @DisplayName("сохранять новый жанр и добавлять его к книге")
    public void addNewGenre() {
        when(genreDao.findByName(GENRE.getName())).thenThrow(new RuntimeException("Genre not found"));
        when(genreDao.save(GENRE)).thenReturn(GENRE);

        var result = service.addBookGenre(BOOK.getId(), GENRE);

        assertThat(result.isOk());
        verify(bookDao, times(1)).addGenre(BOOK.getId(), GENRE);
        verify(genreDao, times(1)).findByName(GENRE.getName());
        verify(genreDao, times(1)).save(GENRE);
    }

    @Test
    @DisplayName("добавлять книге ранее сохраненного автора")
    public void addExistingAuthor() {
        when(authorDao.findByFirstAndLastName(AUTHOR.getFirstName(), AUTHOR.getLastName())).thenReturn(AUTHOR);

        var result = service.addBookAuthor(BOOK.getId(), AUTHOR);

        assertThat(result.isOk());
        verify(bookDao, times(1)).addAuthor(BOOK.getId(), AUTHOR);
        verify(authorDao, times(1)).findByFirstAndLastName(AUTHOR.getFirstName(), AUTHOR.getLastName());
        verify(authorDao, never()).save(AUTHOR);
    }

    @Test
    @DisplayName("сохранять нового автора и добалять его к книге")
    public void addNewAuthor() {
        when(authorDao.findByFirstAndLastName(AUTHOR.getFirstName(), AUTHOR.getLastName()))
            .thenThrow(new RuntimeException("Author not found"));
        when(authorDao.save(AUTHOR)).thenReturn(AUTHOR);

        var result = service.addBookAuthor(BOOK.getId(), AUTHOR);

        assertThat(result.isOk());
        verify(bookDao, times(1)).addAuthor(BOOK.getId(), AUTHOR);
        verify(authorDao, times(1)).findByFirstAndLastName(AUTHOR.getFirstName(), AUTHOR.getLastName());
        verify(authorDao, times(1)).save(AUTHOR);
    }

    @Test
    @DisplayName("отдавать информацию о книге и ее авторах и жанрах")
    public void getInfo() {
        when(bookDao.findById(BOOK.getId())).thenReturn(BOOK);
        when(authorDao.findByBook(BOOK)).thenReturn(singletonList(AUTHOR));
        when(genreDao.findByBook(BOOK)).thenReturn(singletonList(GENRE));

        var result = service.get(BOOK.getId());

        assertThat(result.isOk());

        var book = result.value().get();

        assertThat(book.getId()).isEqualTo(BOOK.getId());
        assertThat(book.getTitle()).isEqualTo(BOOK.getTitle());
        assertThat(book.getAuthors()).isEqualTo(singletonList(AUTHOR));
        assertThat(book.getGenres()).isEqualTo(singletonList(GENRE));
    }

    @Test
    @DisplayName("отдавать пустой результат, если книга не найдена")
    public void emptyResultIfBookNotFound() {
        when(bookDao.findById(BOOK.getId())).thenThrow(new EmptyResultDataAccessException(1));

        var result = service.get(BOOK.getId());

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();

        verify(bookDao, times(1)).findById(BOOK.getId());
        verify(authorDao, never()).findByBook(BOOK);
        verify(genreDao, never()).findByBook(BOOK);
    }

    @Test
    @DisplayName("отдавать ошибку, если произошла иная нештатная ситуация при поиске книги")
    public void failIfDaoFails() {
        when(bookDao.findById(BOOK.getId())).thenThrow(new QueryTimeoutException("Timeout"));

        var result = service.get(BOOK.getId());

        assertThat(result.isOk()).isFalse();

        verify(bookDao, times(1)).findById(BOOK.getId());
        verify(authorDao, never()).findByBook(BOOK);
        verify(genreDao, never()).findByBook(BOOK);
    }

    @Test
    @DisplayName("отдавать информацию о книгах и их авторах и жанрах")
    public void getAllBooksInfo() {
        when(bookDao.findAll()).thenReturn(singletonList(BOOK));
        when(authorDao.findByBook(BOOK)).thenReturn(singletonList(AUTHOR));
        when(genreDao.findByBook(BOOK)).thenReturn(singletonList(GENRE));

        var result = service.getAll();

        assertThat(result.isOk());
        assertThat(result.value().get().size()).isEqualTo(1);

        var book = result.value().get().get(0);

        assertThat(book.getId()).isEqualTo(BOOK.getId());
        assertThat(book.getTitle()).isEqualTo(BOOK.getTitle());
        assertThat(book.getAuthors()).isEqualTo(singletonList(AUTHOR));
        assertThat(book.getGenres()).isEqualTo(singletonList(GENRE));
    }

    @Test
    @DisplayName("вернуть ошибку, если при поиске книг возникает исключение")
    public void failIfBookDaoFailsOnFindAll() {
        when(bookDao.findAll()).thenThrow(new QueryTimeoutException("Timeout"));

        var result = service.getAll();

        assertThat(result.isOk()).isFalse();
    }
}
