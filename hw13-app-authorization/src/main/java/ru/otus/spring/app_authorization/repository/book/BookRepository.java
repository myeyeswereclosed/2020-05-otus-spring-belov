package ru.otus.spring.app_authorization.repository.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PostFilter;
import ru.otus.spring.app_authorization.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
    @Override
    @PostFilter("hasRole('MANAGER') or filterObject.status == 'INIT'")
    List<Book> findAll();
}
