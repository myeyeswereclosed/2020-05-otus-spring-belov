package ru.otus.spring.rest_book_info_app.controller.rest.mapper;

public interface DtoMapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);
}
