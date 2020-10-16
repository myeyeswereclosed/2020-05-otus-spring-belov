package ru.otus.spring.hw18.book_service.controller.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw18.book_service.domain.Genre;
import ru.otus.spring.hw18.lib.GenreDto;

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
