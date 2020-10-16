package ru.otus.spring.hw18.book_service.service.book.info.edit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw18.book_service.domain.Author;
import ru.otus.spring.hw18.book_service.domain.Genre;
import ru.otus.spring.hw18.book_service.infrastructure.AppLogger;
import ru.otus.spring.hw18.book_service.infrastructure.AppLoggerFactory;
import ru.otus.spring.hw18.book_service.repository.author.AuthorRepository;
import ru.otus.spring.hw18.book_service.repository.book.BookInfoRepository;
import ru.otus.spring.hw18.book_service.repository.genre.GenreRepository;
import ru.otus.spring.hw18.book_service.service.result.Executed;
import ru.otus.spring.hw18.book_service.service.result.Failed;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

@RequiredArgsConstructor
@Service
public class EditBookInfoServiceImpl implements EditBookInfoService {
    private static final AppLogger logger = AppLoggerFactory.logger(EditBookInfoServiceImpl.class);

    private final BookInfoRepository bookInfoRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public ServiceResult<Author> renameAuthor(Author author) {
        try {
            if (authorAlreadyExists(author)) {
                return
                    new Failed<>(
                        "Cannot rename author, cause author '" + author.fullName() +
                            "' already stored with another id"
                    );
            }

            if (authorRepository.existsById(author.getId())) {
                authorRepository.update(author);
                bookInfoRepository.updateAuthor(author);

                return new Executed<>(author);
            }

            logger.warn("Author with id = {} not found", author.getId());

            return Executed.empty();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<String> removeAuthorById(String id) {
        try {
            return
                authorRepository
                    .delete(id)
                    .map(
                        deleted -> {
                            bookInfoRepository.removeAuthor(id);

                            return new Executed<>(id);
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
    @Transactional
    public ServiceResult<Genre> renameGenre(Genre genre) {
        try {
            if (genreAlreadyExists(genre)) {
                return
                    new Failed<>(
                        "Cannot rename author, cause genre '" + genre.getName() +
                            "' already stored with another id"
                    );
            }

            if (genreRepository.existsById(genre.getId())) {
                genreRepository.update(genre);
                bookInfoRepository.updateGenre(genre);

                return new Executed<>(genre);
            }

            logger.warn("Genre with id = {} not found", genre.getId());

            return Executed.empty();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<String> removeGenreById(String id) {
        try {
            return
                genreRepository
                    .delete(id)
                    .map(
                        deleted -> {
                            bookInfoRepository.removeGenre(id);

                            return new Executed<>(id);
                        }
                    )
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private boolean authorAlreadyExists(Author author) {
        return
            authorRepository
                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName())
                .isPresent()
            ;
    }

    private boolean genreAlreadyExists(Genre genre) {
        return
            genreRepository
                .findByName(genre.getName())
                .isPresent()
            ;
    }
}
