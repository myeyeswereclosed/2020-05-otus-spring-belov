package ru.otus.spring.app_container.controller.rest.mapper;

public interface DtoMapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);
}
