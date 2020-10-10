package ru.otus.spring.actuator.controller.rest.mapper;

public interface DtoMapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);
}
