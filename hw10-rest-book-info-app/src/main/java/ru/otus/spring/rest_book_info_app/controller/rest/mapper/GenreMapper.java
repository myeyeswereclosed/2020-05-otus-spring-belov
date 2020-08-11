package ru.otus.spring.rest_book_info_app.controller.rest.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.otus.spring.rest_book_info_app.controller.rest.dto.GenreDto;
import ru.otus.spring.rest_book_info_app.domain.Genre;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Component
public class GenreMapper implements DtoMapper<Genre, GenreDto> {
    private final ModelMapper modelMapper;

    @Override
    public Genre toEntity(@NotNull GenreDto dto) {
        return modelMapper.map(dto, Genre.class);
    }

    @Override
    public GenreDto toDto(@NotNull Genre genre) {
        return modelMapper.map(genre, GenreDto.class);
    }
}
