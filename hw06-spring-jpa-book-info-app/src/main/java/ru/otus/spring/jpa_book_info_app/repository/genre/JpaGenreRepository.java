package ru.otus.spring.jpa_book_info_app.repository.genre;

import org.springframework.stereotype.Repository;
import ru.otus.spring.jpa_book_info_app.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaGenreRepository implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Genre save(Genre genre) {
        if (genre.hasNoId()) {
            em.persist(genre);
            return genre;
        }

        return em.merge(genre);
    }

    @Override
    public boolean delete(int id) {
        Query query = em.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);

        return query.executeUpdate() > 0;
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public Optional<Genre> findByName(String name) {
        var query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);

        query.setParameter("name", name);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}