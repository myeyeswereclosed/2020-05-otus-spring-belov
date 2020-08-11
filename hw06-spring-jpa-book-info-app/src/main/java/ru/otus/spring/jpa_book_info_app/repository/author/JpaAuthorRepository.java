package ru.otus.spring.jpa_book_info_app.repository.author;

import org.springframework.stereotype.Repository;
import ru.otus.spring.jpa_book_info_app.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public boolean delete(long id) {
        Query query = em.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);

        return query.executeUpdate() > 0;
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Optional<Author> findByFirstAndLastName(String firstName, String lastName) {
        var query =
            em.createQuery(
                "select a from Author a where a.firstName = :firstName and a.lastName = :lastName",
                Author.class
            );

        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}