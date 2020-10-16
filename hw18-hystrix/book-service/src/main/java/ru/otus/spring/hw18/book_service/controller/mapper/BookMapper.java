package ru.otus.spring.hw18.book_service.controller.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw18.book_service.domain.Author;
import ru.otus.spring.hw18.book_service.domain.Book;
import ru.otus.spring.hw18.book_service.domain.Genre;
import ru.otus.spring.hw18.lib.AuthorDto;
import ru.otus.spring.hw18.lib.BookDto;
import ru.otus.spring.hw18.lib.GenreDto;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class BookMapper implements DtoMapper<Book, BookDto> {
    private final DtoMapper<Author, AuthorDto> authorMapper;
    private final DtoMapper<Genre, GenreDto> genreMapper;

    @Override
    public Book toEntity(@NonNull BookDto dto) {
        return
            new Book(
                dto.getId(),
                dto.getTitle(),
                dto.getAuthors().stream().map(authorMapper::toEntity).collect(toList()),
                dto.getGenres().stream().map(genreMapper::toEntity).collect(toList())
            );
    }

    @Override
    public BookDto toDto(@NonNull Book book) {
        return
            new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthors().stream().map(authorMapper::toDto).collect(toList()),
                book.getGenres().stream().map(genreMapper::toDto).collect(toList())
            );
    }
}
