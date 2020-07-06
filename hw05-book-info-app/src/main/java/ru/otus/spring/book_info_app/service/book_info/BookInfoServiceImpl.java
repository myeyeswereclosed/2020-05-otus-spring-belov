package ru.otus.spring.book_info_app.service.book_info;

import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.domain.BookInfo;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.service.author.AuthorService;
import ru.otus.spring.book_info_app.service.book.BookService;
import ru.otus.spring.book_info_app.service.genre.GenreService;
import ru.otus.spring.book_info_app.service.result.FailResult;
import ru.otus.spring.book_info_app.service.result.ServiceResult;
import ru.otus.spring.book_info_app.service.result.SuccessResult;

import static java.util.Collections.emptyList;

@Service
public class BookInfoServiceImpl implements BookInfoService {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookInfoServiceImpl(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    public ServiceResult<Void> addAuthor(long bookId, Name name) {
        return
            authorService
                .getByName(name)
                .value()
                .map(existingAuthor -> bookService.addAuthor(bookId, existingAuthor))
                .orElseGet(
                    () ->
                        authorService
                            .create(name)
                            .value()
                            .map(newAuthor -> bookService.addAuthor(bookId, newAuthor))
                            .orElse(new FailResult<>())
                );
    }

    @Override
    public ServiceResult<Void> addGenre(long bookId, String name) {
        return
            genreService
                .getByName(name)
                .value()
                .map(genre -> bookService.addGenre(bookId, genre))
                .orElseGet(
                    () ->
                        genreService
                            .create(name)
                            .value()
                            .map(newGenre -> bookService.addGenre(bookId, newGenre))
                            .orElse(new FailResult<>())
                );
    }

    @Override
    public ServiceResult<BookInfo> get(long bookId) {
        return
            bookService
                .find(bookId)
                .value()
                .<ServiceResult<BookInfo>>map(
                    book ->
                        new SuccessResult<>(
                            new BookInfo(
                                book,
                                authorService.getByBook(book).value().orElse(emptyList()),
                                genreService.getByBook(book).value().orElse(emptyList())
                            )
                        )
                )
                .orElseGet(FailResult::new)
        ;
    }
}
