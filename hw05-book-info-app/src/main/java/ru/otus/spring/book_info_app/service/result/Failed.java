package ru.otus.spring.book_info_app.service.result;

import java.util.*;

public class Failed<T> implements ServiceResult<T> {
    private List<Throwable> failures = new ArrayList<>();

    public Failed(Throwable failure) {
        if (Objects.nonNull(failure)) {
            failures.add(failure);
        }
    }

    public static<T, U> Failed<T> fromResult(ServiceResult<U> result, Throwable failure) {
        var failed = new Failed<T>(failure);

        result.failures().forEach(previousFailure -> failed.failures().add(previousFailure));

        return failed;
    }

    @Override
    public Optional<T> value() {
        return Optional.empty();
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public List<Throwable> failures() {
        return failures;
    }
}
