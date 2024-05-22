package org.ttaaa.backendhw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ttaaa.backendhw.model.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
    List<Genre> findAllByIdIn(List<UUID> uuids);
    Optional<Genre> findByName(String name);
}
