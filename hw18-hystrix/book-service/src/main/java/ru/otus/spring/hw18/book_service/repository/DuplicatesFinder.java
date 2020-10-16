package ru.otus.spring.hw18.book_service.repository;

import java.util.List;

public interface DuplicatesFinder<T> {
    List<T> findDuplicates();
}
