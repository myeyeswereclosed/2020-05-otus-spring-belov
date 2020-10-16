package ru.otus.spring.hw18.book_service.repository.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.hw18.book_service.domain.Book;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
}
