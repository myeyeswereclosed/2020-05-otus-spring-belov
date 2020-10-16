package ru.otus.spring.hw18.book_service.controller.mapper;

public interface DtoMapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);
}
