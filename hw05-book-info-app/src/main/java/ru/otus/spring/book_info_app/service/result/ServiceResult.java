package ru.otus.spring.book_info_app.service.result;

import java.util.List;
import java.util.Optional;

public interface ServiceResult<T> {
    Optional<T> value();

    boolean isOk();

    // обращение к этому методу позволит избавиться от метода isOk()
    List<Throwable> failures();
}
