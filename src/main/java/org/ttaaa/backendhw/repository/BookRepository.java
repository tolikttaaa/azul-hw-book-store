package org.ttaaa.backendhw.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttaaa.backendhw.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.author.id = :authorId")
    Optional<Book> getByUniqueParams(
            @Param("title") String title,
            @Param("authorId") UUID authorId);
}
