package ru.otus.spring.actuator.repository;

import java.util.List;

public interface DuplicatesFinder<T> {
    List<T> findDuplicates();
}
