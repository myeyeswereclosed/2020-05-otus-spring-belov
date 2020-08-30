package ru.otus.spring.reactive_book_info_app.repository.book;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.reactive_book_info_app.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {
}
