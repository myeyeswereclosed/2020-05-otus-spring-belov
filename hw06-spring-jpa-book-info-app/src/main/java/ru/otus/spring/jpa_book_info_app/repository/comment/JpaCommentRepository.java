package ru.otus.spring.jpa_book_info_app.repository.comment;

import org.springframework.stereotype.Repository;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentRepository implements CommentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment save(Comment comment) {
        em.persist(comment);

        return comment;
    }

    @Override
    public void update(Comment comment) {
        Query query = em.createQuery("update Comment c set c.text = :text where c.id = :id");
        query.setParameter("id", comment.getId());
        query.setParameter("text", comment.getText());
        query.executeUpdate();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBook(long bookId) {
        var query = em.createQuery("select c from Comment c where c.book.id = :id", Comment.class);

        query.setParameter("id", bookId);

        return query.getResultList();
    }

    @Override
    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c join fetch c.book", Comment.class).getResultList();
    }

    @Override
    public boolean delete(long id) {
        Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate() > 0;
    }
}
