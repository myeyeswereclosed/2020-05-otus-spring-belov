package ru.otus.spring.app_authorization.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NotFoundEntity {
    private final String name;
    private final String id;
}
