package ru.otus.spring.jpa_book_info_app.service.book;

import org.springframework.stereotype.Service;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.dto.BookDto;
import ru.otus.spring.jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.jpa_book_info_app.repository.book.BookRepository;
import ru.otus.spring.jpa_book_info_app.service.result.Executed;
import ru.otus.spring.jpa_book_info_app.service.result.Failed;
import ru.otus.spring.jpa_book_info_app.service.result.ServiceResult;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookInfoServiceImpl implements BookInfoService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookInfoServiceImpl.class);

    private final BookRepository bookRepository;

    public BookInfoServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

//    @Override
//    public ServiceResult<Void> addBookAuthor(long bookId, Author author) {
//        Author existingAuthor;
//
//        try {
//            existingAuthor = authorDao.findByFirstAndLastName(author.getFirstName(), author.getLastName());
//        } catch (Exception e) {
//            logger.warn(e.getMessage());
//
//            return addNewAuthor(bookId, author);
//        }
//
//        return
//            Optional
//                .ofNullable(existingAuthor)
//                .map(existingOne -> addAuthor(bookId, existingOne))
//                .orElseGet(() -> addNewAuthor(bookId, author))
//        ;
//    }
//
//    private ServiceResult<Void> addNewAuthor(long bookId, Author author) {
//        return
//            Optional
//                .ofNullable(authorDao.save(author))
//                .map(newAuthor -> addAuthor(bookId, newAuthor))
//                .orElseGet(Failed::new)
//        ;
//    }
//
//    private ServiceResult<Void> addAuthor(long bookId, Author author) {
//        try {
//            bookRepository.addAuthor(bookId, author);
//
//            return Executed.unit();
//        } catch (Exception e) {
//            logger.logException(e);
//        }
//
//        return new Failed<>();
//    }
//
//    @Override
//    public ServiceResult<Void> addBookGenre(long bookId, Genre genre) {
//        Genre existingGenre;
//
//        try {
//           existingGenre = genreDao.findByName(genre.getName());
//        } catch (Exception e) {
//            logger.warn(e.getMessage());
//
//            return addNewGenre(bookId, genre);
//        }
//
//        return
//            Optional
//                .ofNullable(existingGenre)
//                .map(existingOne -> addGenre(bookId, existingOne))
//                .orElseGet(Failed::new)
//            ;
//    }
//
//    private ServiceResult<Void> addNewGenre(long bookId, Genre genre) {
//        return
//            Optional
//                .ofNullable(genreDao.save(genre))
//                .map(newGenre -> addGenre(bookId, newGenre))
//                .orElseGet(Failed::new)
//        ;
//    }
//
//    private ServiceResult<Void> addGenre(long bookId, Genre genre) {
//        try {
//            bookRepository.addGenre(bookId, genre);
//
//            return Executed.unit();
//        } catch (Exception e) {
//            logger.logException(e);
//        }
//
//        return new Failed<>();
//    }

    @Override
    @Transactional
    public ServiceResult<Book> get(long bookId) {
        return
            bookRepository
                .findById(bookId)
                .<ServiceResult<Book>>map(
                    book -> {
                        logger.getLogger().info("Found book {}", book);

                        return new Executed<>(book);
                    }
                )
                .orElseGet(Failed::new)
        ;
    }

    @Override
    @Transactional
    public ServiceResult<List<Book>> getAll() {
        try {
            var books = bookRepository.findAll();

            logger.getLogger().info("Found {} books", books.size());

            return new Executed<>(books);
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }
}
