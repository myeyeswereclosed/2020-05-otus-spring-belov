package ru.otus.spring.app_container.service.result;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
public class Failed<T> implements ServiceResult<T> {
    private Map<byte[], byte[]> m = new HashMap<>();



    private String description = "";

    public Failed(String description) {
        this.description = description;
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
    public String description() {
        return description;
    }
}


