package ru.otus.spring.spring_integration.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class Callback {
    private final UUID id;
    private final Transaction transaction;
}
