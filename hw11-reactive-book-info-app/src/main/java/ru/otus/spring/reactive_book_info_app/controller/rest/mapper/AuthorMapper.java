package ru.otus.spring.reactive_book_info_app.controller.rest.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.otus.spring.reactive_book_info_app.controller.rest.dto.AuthorDto;
import ru.otus.spring.reactive_book_info_app.domain.Author;

@RequiredArgsConstructor
@Component
public class AuthorMapper implements DtoMapper<Author, AuthorDto> {
    private final ModelMapper modelMapper;

    @Override
    public Author toEntity(@NonNull AuthorDto dto) {
        return modelMapper.map(dto, Author.class);
    }

    @Override
    public AuthorDto toDto(@NonNull Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }
}
