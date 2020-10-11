package ru.otus.spring.app_container.repository.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.app_container.domain.Book;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
}
