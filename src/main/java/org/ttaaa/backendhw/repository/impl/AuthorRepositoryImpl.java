package org.ttaaa.backendhw.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.repository.AuthorRepository;
import org.ttaaa.backendhw.repository.AbstractRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthorRepositoryImpl extends AbstractRepository implements AuthorRepository {
    @Override
    @Transactional
    public Author save(Author entity) {
        Optional<Author> existing = getByUniqueParams(entity.getFirstName(), entity.getLastName(), entity.getMidName());
        if (existing.isPresent()) throw new BadRequestException.AuthorBadRequestException("Author already exists", existing.get());

        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public Author update(Author entity) {
        getById(entity.getId());
        Optional<Author> existing = getByUniqueParams(entity.getFirstName(), entity.getLastName(), entity.getMidName());
        if (existing.isPresent()) throw new BadRequestException.AuthorBadRequestException("Author with such params already exists", existing.get());

        entityManager.merge(entity);
        return entity;
    }

    @Override
    public Author getById(UUID uuid) {
        Author entity = entityManager.find(Author.class, uuid);
        if (entity == null) throw new NotFoundException.AuthorNotFoundException(uuid);
        return entity;
    }

    private Optional<Author> getByUniqueParams(String firstName, String lastName, String midName) {
        return entityManager.createQuery("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName AND a.midName = :midName", Author.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setParameter("midName", midName)
                .getResultStream().findFirst();
    }

    @Override
    public List<Author> getAll() {
        return entityManager.createQuery("SELECT a FROM Author a", Author.class).getResultList();
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        entityManager.remove(getById(uuid));
    }
}
