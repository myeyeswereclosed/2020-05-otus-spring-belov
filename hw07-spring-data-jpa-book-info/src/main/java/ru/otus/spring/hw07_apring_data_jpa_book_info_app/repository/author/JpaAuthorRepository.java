package ru.otus.spring.hw07_apring_data_jpa_book_info_app.repository.author;

import org.springframework.stereotype.Repository;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.dto.BookAuthor;

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
    public List<BookAuthor> findAllWithBooks() {
        return
            em
                .createNativeQuery(
                    "select a.id author_id, a.first_name, a.last_name, ba.book_id book_id " +
                    "from author a join book_author ba " +
                    "on ba.author_id = a.id",
                    "BookAuthorMapping"
                )
                .getResultList()
        ;
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