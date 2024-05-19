package org.ttaaa.backendhw.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.repository.AbstractRepository;
import org.ttaaa.backendhw.repository.GenreRepository;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.entity.Genre;

@Repository
public class GenreRepositoryImpl extends AbstractRepository implements GenreRepository {
    @Override
    @Transactional
    public Genre save(Genre entity) {
        Optional<Genre> existing = getByUniqueParams(entity);
        if (existing.isPresent()) throw new BadRequestException.GenreBadRequestException("Genre already exists", existing.get());

        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public Genre update(Genre entity) {
        getById(entity.getId());
        Optional<Genre> existing = getByUniqueParams(entity);
        if (existing.isPresent()) throw new BadRequestException.GenreBadRequestException("Genre already exists", existing.get());

        entityManager.merge(entity);
        return entity;
    }

    @Override
    public Genre getById(UUID uuid) {
        Genre entity = entityManager.find(Genre.class, uuid);
        if (entity == null) throw new NotFoundException.GenreNotFoundException(uuid);
        return entity;
    }

    @Override
    public Set<Genre> getByIds(List<UUID> uuids) {
        return entityManager.createQuery("SELECT g FROM Genre g WHERE g.id IN :ids", Genre.class)
                .setParameter("ids", uuids)
                .getResultStream().collect(Collectors.toSet());
    }

    private Optional<Genre> getByUniqueParams(Genre filter) {
        return entityManager.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                .setParameter("name", filter.getName())
                .getResultStream().findFirst();
    }

    @Override
    public List<Genre> getAll() {
        return entityManager.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        entityManager.remove(getById(uuid));
    }
}
