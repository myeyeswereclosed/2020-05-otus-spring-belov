package ru.otus.spring.actuator.repository;

import java.util.List;

public interface FindDuplicates<T> {
    List<T> findDuplicates();
}
