package ru.otus.spring.book_info_app.service.book;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.dao.author.AuthorDao;
import ru.otus.spring.book_info_app.dao.book.BookDao;
import ru.otus.spring.book_info_app.dao.genre.GenreDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.infrastructure.AppLogger;
import ru.otus.spring.book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.book_info_app.service.result.Executed;
import ru.otus.spring.book_info_app.service.result.Failed;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.*;

import static java.util.Comparator.comparingLong;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import static java.util.Collections.emptyList;

@Service
public class BookInfoServiceImpl implements BookInfoService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookInfoServiceImpl.class);

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookInfoServiceImpl(
        BookDao bookDao,
        AuthorDao authorDao,
        GenreDao genreDao
    ) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public ServiceResult<Void> addBookAuthor(long bookId, Author author) {
        Author existingAuthor;

        try {
            existingAuthor = authorDao.findByFirstAndLastName(author.getFirstName(), author.getLastName());
        } catch (Exception e) {
            logger.warn(e.getMessage());

            return addNewAuthor(bookId, author);
        }

        return
            Optional
                .ofNullable(existingAuthor)
                .map(existingOne -> addAuthor(bookId, existingOne))
                .orElseGet(() -> addNewAuthor(bookId, author))
            ;
    }

    private ServiceResult<Void> addNewAuthor(long bookId, Author author) {
        return
            Optional
                .ofNullable(authorDao.save(author))
                .map(newAuthor -> addAuthor(bookId, newAuthor))
                .orElseGet(Failed::new)
            ;
    }

    private ServiceResult<Void> addAuthor(long bookId, Author author) {
        try {
            bookDao.addAuthor(bookId, author);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    public ServiceResult<Void> addBookGenre(long bookId, Genre genre) {
        Genre existingGenre;

        try {
            existingGenre = genreDao.findByName(genre.getName());
        } catch (Exception e) {
            logger.warn(e.getMessage());

            return addNewGenre(bookId, genre);
        }

        return
            Optional
                .ofNullable(existingGenre)
                .map(existingOne -> addGenre(bookId, existingOne))
                .orElseGet(Failed::new)
            ;
    }

    private ServiceResult<Void> addNewGenre(long bookId, Genre genre) {
        return
            Optional
                .ofNullable(genreDao.save(genre))
                .map(newGenre -> addGenre(bookId, newGenre))
                .orElseGet(Failed::new)
            ;
    }

    private ServiceResult<Void> addGenre(long bookId, Genre genre) {
        try {
            bookDao.addGenre(bookId, genre);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    public ServiceResult<Book> get(long bookId) {
        try {
            return
                Optional
                    .ofNullable(bookDao.findById(bookId))
                    .flatMap(this::getBookWithInfo)
                    .<ServiceResult<Book>>map(Executed::new)
                    .orElseGet(Failed::new);
        } catch (EmptyResultDataAccessException e) {
            logger.warn(e.getMessage());

            return new Executed<>(null);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private Optional<Book> getBookWithInfo(Book book) {
        try {
            return
                Optional.of(
                    new Book(
                        book.getId(),
                        book.getTitle(),
                        authorDao.findByBook(book),
                        genreDao.findByBook(book)
                    )
                );
        } catch (Exception e) {
            logger.logException(e);
        }

        return Optional.empty();
    }

    @Override
    public ServiceResult<List<Book>> getAll() {
        try {
            var books = bookDao.findAll();

            return
                new Executed<>(
                    books.isEmpty()
                        ? books
                        : booksWithInfo(books)
                );
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    private List<Book> booksWithInfo(List<Book> books) {
        var authors = bookIdToItsInfo(authorDao.findAllWithBooks());
        var genres = bookIdToItsInfo(genreDao.findAllWithBooks());

        return
            books
                .parallelStream()
                .peek(
                    book -> {
                        book.setAuthors(findOrEmpty(authors, book.getId()));
                        book.setGenres(findOrEmpty(genres, book.getId()));
                    }
                )
                .sorted(comparingLong(Book::getId))
                .collect(toList())
            ;
    }

    private<T> List<T> findOrEmpty(Map<Long, List<T>> valuesMap, Long key) {
        return valuesMap.getOrDefault(key, emptyList());
    }

    private<T> Map<Long, List<T>> bookIdToItsInfo(List<Pair<T, Long>> infoToBookIdItems) {
        return
            infoToBookIdItems
                .parallelStream()
                .collect(
                    groupingBy(
                        Pair::getRight,
                        mapping(Pair::getLeft, toList())
                    )
                );
    }
}
