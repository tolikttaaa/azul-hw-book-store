package org.ttaaa.backendhw.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.repository.AbstractRepository;
import org.ttaaa.backendhw.repository.BookRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public class BookRepositoryImpl extends AbstractRepository implements BookRepository {
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
        return entityManager.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        entityManager.remove(getById(uuid));
    }

    @Override
    @Transactional
    public Book updateBookTitle(UUID id, String title) {
        Book entity = getById(id);
        entity.setTitle(title);
        entityManager.merge(entity);

        return entity;
    }

    @Override
    @Transactional
    public Book updateBookPrice(UUID id, Double price) {
        Book entity = getById(id);
        entity.setPrice(price);
        entityManager.merge(entity);

        return entity;
    }

    @Override
    @Transactional
    public Book updateBookAuthor(UUID id, Author author) {
        Book entity = getById(id);
        entity.setAuthor(author);
        entityManager.merge(entity);

        return entity;
    }

    @Override
    @Transactional
    public Book updateBookGenres(UUID id, Set<Genre> genres) {
        Book entity = getById(id);
        entity.setGenres(genres);
        entityManager.merge(entity);

        return entity;
    }
}
