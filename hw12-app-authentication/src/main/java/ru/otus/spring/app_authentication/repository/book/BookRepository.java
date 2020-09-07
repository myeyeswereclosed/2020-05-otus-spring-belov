package ru.otus.spring.app_authentication.repository.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.app_authentication.domain.Book;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
}
