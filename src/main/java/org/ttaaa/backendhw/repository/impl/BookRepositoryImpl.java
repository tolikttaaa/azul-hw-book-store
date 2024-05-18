package org.ttaaa.backendhw.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.repository.AbstractRepository;
import org.ttaaa.backendhw.repository.BookRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class BookRepositoryImpl extends AbstractRepository implements BookRepository {
    private final String PQL_GET_ALL = "SELECT b FROM Book b";

    @Override
    @Transactional
    public Book save(Book entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public Book update(Book entity) {
        getById(entity.getId());
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public Book getById(UUID uuid) {
        Book entity = entityManager.find(Book.class, uuid);
        if (entity == null) throw new NotFoundException.BookNotFoundException(uuid);
        return entity;
    }

    @Override
    public List<Book> getAll() {
        return entityManager.createQuery(PQL_GET_ALL, Book.class).getResultList();
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        entityManager.remove(getById(uuid));
    }
}
