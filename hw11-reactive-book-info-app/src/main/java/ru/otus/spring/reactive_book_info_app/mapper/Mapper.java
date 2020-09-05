package ru.otus.spring.reactive_book_info_app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
