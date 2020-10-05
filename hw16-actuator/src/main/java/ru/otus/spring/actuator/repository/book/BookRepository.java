package ru.otus.spring.actuator.repository.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.actuator.domain.Book;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
}
