package ru.otus.spring.app_container.repository;

import java.util.List;

public interface DuplicatesFinder<T> {
    List<T> findDuplicates();
}
