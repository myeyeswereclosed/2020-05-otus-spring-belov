package ru.otus.spring.spring_integration.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class Merchant {
    private final UUID id;
    private final String name;
    private final URI callbackUrl;
}
