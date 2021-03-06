package ru.otus.spring.rest_book_info_app.repository.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.rest_book_info_app.domain.Book;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
}
