package ru.otus.spring.spring_data_jpa_book_info_app.repository.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.spring_data_jpa_book_info_app.dto.BookGenre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Modifying
    @Query("update Genre g set g.name = :name where g.id = :id")
    int updateNameById(@Param("id") int id, @Param("name") String name);

    @Query(name = "bookGenres", nativeQuery = true)
    List<BookGenre> findAllWithBooks();

    Optional<Genre> findByName(String name);
}
