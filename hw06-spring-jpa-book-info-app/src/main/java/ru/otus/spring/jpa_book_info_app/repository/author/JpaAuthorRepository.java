package ru.otus.spring.jpa_book_info_app.repository.author;

import org.springframework.stereotype.Repository;
import ru.otus.spring.jpa_book_info_app.domain.Author;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.repository.book.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaAuthorRepository implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Author save(Author author) {
        if (author.hasNoId()) {
            em.persist(author);
            return author;
        }

        return em.merge(author);
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public void delete(long id) {
        Query query = em.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }
}