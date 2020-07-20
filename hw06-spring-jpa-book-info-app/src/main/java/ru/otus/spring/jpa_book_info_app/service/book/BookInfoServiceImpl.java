package ru.otus.spring.jpa_book_info_app.service.book;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.jpa_book_info_app.domain.Author;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.domain.Comment;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.dto.BookInfo;
import ru.otus.spring.jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.jpa_book_info_app.repository.author.AuthorRepository;
import ru.otus.spring.jpa_book_info_app.repository.book.BookRepository;
import ru.otus.spring.jpa_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.jpa_book_info_app.repository.genre.GenreRepository;
import ru.otus.spring.jpa_book_info_app.service.result.Executed;
import ru.otus.spring.jpa_book_info_app.service.result.Failed;
import ru.otus.spring.jpa_book_info_app.service.result.ServiceResult;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.*;

@Service
public class BookInfoServiceImpl implements BookInfoService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookInfoServiceImpl.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    public BookInfoServiceImpl(
        BookRepository bookRepository,
        AuthorRepository authorRepository,
        GenreRepository genreRepository,
        CommentRepository commentRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ServiceResult<Book> addBookAuthor(long bookId, Author author) {
        try {
            return
                bookRepository
                    .findById(bookId)
                    .map(book -> addAuthor(book, author))
                    .orElse(Executed.empty())
            ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> addAuthor(@NotNull Book book, @NotNull Author author) {
        try {
            if (book.isWrittenBy(author)) {
                return addExistingInfo(book, "Book {} already has author {}", author.fullName());
            }

            return
                authorRepository
                    .findByFirstAndLastName(author.getFirstName(), author.getLastName())
                    .map(
                        authorFound ->
                            updateBookAuthor(book, authorFound, "Added existing author {} as author of '{}'")
                    )
                    .orElseGet(() -> updateBookAuthor(book, author, "Added new author {} as author of '{}'"))
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private<T> ServiceResult<Book> addExistingInfo(Book book, String logMessage, String info) {
        logger.warn(logMessage, book.toString(), info);

        return new Executed<>(book);
    }

    private ServiceResult<Book> updateBookAuthor(Book book, Author author, String logMessage) {
        try {
            var updatedBook = bookRepository.save(book.addAuthor(author));

            logger.info(logMessage, author.fullName(), updatedBook.toString());

            return new Executed<>(updatedBook);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<Book> addBookGenre(long bookId, Genre genre) {
        try {
            return
                bookRepository
                    .findById(bookId)
                    .map(book -> addGenre(book, genre))
                    .orElse(Executed.empty())
            ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> addGenre(@NotNull Book book, @NotNull Genre genre) {
        try {
            if (book.hasGenre(genre)) {
                return addExistingInfo(book, "Book {} already has genre {}", genre.getName());
            }

            return
                genreRepository
                    .findByName(genre.getName())
                    .map(genreFound -> updateBookGenre(book, genreFound, "Added '{}' as genre of '{}'"))
                    .orElseGet(() -> updateBookGenre(book, genre, "Added new genre '{}' as genre of '{}'"))
            ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> updateBookGenre(Book book, Genre genre, String logMessage) {
        var updatedBook = bookRepository.save(book.addGenre(genre));

        logger.info(logMessage, genre.getName(), updatedBook.toString());

        return new Executed<>(updatedBook);
    }

    @Override
    @Transactional
    public ServiceResult<Void> addComment(long bookId, Comment comment) {
        try {
            return
                bookRepository.findById(bookId)
                    .<ServiceResult<Void>>map(
                        book -> {
                            comment.setBook(book);

                            commentRepository.save(comment);

                            logger.info("Added '{}' as comment to '{}'", comment.getText(), book.getTitle());

                            return Executed.unit();
                        }
                    )
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<BookInfo> get(long bookId) {
        try {
            return
                bookRepository
                    .findById(bookId)
                    .<ServiceResult<BookInfo>>map(
                        book -> {
                            logger.getLogger().info("Found book {}", book);

                            return new Executed<>(
                                new BookInfo(
                                    book,
                                    new HashSet<>(commentRepository.findByBook(bookId))
                                )
                            );
                        })
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    public ServiceResult<List<BookInfo>> getAll() {
        try {
            var booksStored = bookRepository.findAll();
            var bookIdsMappedToAuthors = bookIdsMappedToAuthors();
            var bookIdsMappedToGenres = bookIdsMappedToGenres();
            var comments = comments();

            var books =
                booksStored
                    .parallelStream()
                    .map(
                        book -> {
                            book.setAuthors(findOrEmpty(bookIdsMappedToAuthors, book.getId()));
                            book.setGenres(findOrEmpty(bookIdsMappedToGenres, book.getId()));

                            return
                                new BookInfo(
                                    book,
                                    comments.getOrDefault(book.getId(), emptySet())
                                );
                        }
                    )
                    .sorted(comparingLong(BookInfo::bookId))
                    .collect(toList())
                ;

            logger.getLogger().info("Found {} books", books.size());

            return new Executed<>(books);
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    private Map<Long, Set<Author>> bookIdsMappedToAuthors() {
        return
            toMap(
                authorRepository.findAllWithBooks(),
                bookAuthor ->
                    Pair.of(
                        bookAuthor.getBookId(),
                        new Author(
                            bookAuthor.getAuthorId(),
                            bookAuthor.getAuthorFirstName(),
                            bookAuthor.getAuthorLastName()
                        )
                    )
            );
    }

    private Map<Long, Set<Genre>> bookIdsMappedToGenres() {
        return
            toMap(
                genreRepository.findAllWithBooks(),
                bookGenre ->
                    Pair.of(
                        bookGenre.getBookId(),
                        new Genre(bookGenre.getGenreId(), bookGenre.getGenreName())
                    )
            );
    }

    private Map<Long, Set<Comment>> comments() {
        return
            commentRepository
                .findAll()
                .parallelStream()
                .collect(
                    Collectors.groupingBy(
                        comment -> comment.getBook().getId(),
                        Collectors.toSet()
                    )
                )
            ;
    }

    private<T, U> Map<Long, Set<U>> toMap(List<T> values, Function<T, Pair<Long, U>> mapper) {
        return
            values
                .parallelStream()
                .map(mapper)
                .collect(
                    groupingBy(
                        Pair::getLeft,
                        mapping(Pair::getRight, toSet())
                    )
                );
    }

    private<T> Set<T> findOrEmpty(Map<Long, Set<T>> valuesMap, Long key) {
        return valuesMap.getOrDefault(key, emptySet());
    }
}
