package ru.otus.spring.jpa_book_info_app.repository.book;

import org.springframework.stereotype.Repository;
import ru.otus.spring.jpa_book_info_app.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookRepository implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.hasNoId()) {
            em.persist(book);
            return book;
        }

        return em.merge(book);
    }

    @Override
    public Optional<Book> findById(long id) {
        var query =
            em.createQuery(
                "select distinct b from Book b " +
                "left join fetch b.authors " +
                "left join fetch b.genres " +
                "where b.id = :id",
                Book.class
            );

        query.setParameter("id", id);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long id) {
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate() > 0;
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("select b from Book b", Book.class).getResultList();
    }
}