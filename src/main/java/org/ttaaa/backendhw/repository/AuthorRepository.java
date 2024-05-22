package org.ttaaa.backendhw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttaaa.backendhw.model.entity.Author;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    @Query("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName AND a.midName = :midName")
    Optional<Author> getByUniqueParams(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("midName") String midName);
}
