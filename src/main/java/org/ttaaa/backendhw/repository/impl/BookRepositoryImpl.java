package org.ttaaa.backendhw.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.repository.AbstractRepository;
import org.ttaaa.backendhw.repository.BookRepository;

import java.util.*;
import java.util.stream.Stream;

@Repository
public class BookRepositoryImpl extends AbstractRepository implements BookRepository {
    @Override
    @Transactional
    public Book save(Book entity) {
        Optional<Book> existing = getByUniqueParams(entity.getTitle(), entity.getAuthor().getId());
        if (existing.isPresent()) throw new BadRequestException.BookBadRequestException("Book already exists", existing.get());

        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public Book update(Book entity) {
        getById(entity.getId());
        Optional<Book> existing = getByUniqueParams(entity.getTitle(), entity.getAuthor().getId());
        if (existing.isPresent()) throw new BadRequestException.BookBadRequestException("Book with such params already exists", existing.get());

        entityManager.merge(entity);
        return entity;
    }

    @Override
    public Book getById(UUID uuid) {
        Book entity = entityManager.find(Book.class, uuid);
        if (entity == null) throw new NotFoundException.BookNotFoundException(uuid);
        return entity;
    }

    private Optional<Book> getByUniqueParams(String title, UUID authorId) {
        return entityManager.createQuery("SELECT b FROM Book b LEFT JOIN b.author a WHERE b.title = :title AND a.id = :authorId", Book.class)
                .setParameter("title", title)
                .setParameter("authorId", authorId)
                .getResultStream().findFirst();
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

        Optional<Book> existing = getByUniqueParams(entity.getTitle(), entity.getAuthor().getId());
        if (existing.isPresent()) throw new BadRequestException.BookBadRequestException("Book with such params already exists", existing.get());
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

        Optional<Book> existing = getByUniqueParams(entity.getTitle(), entity.getAuthor().getId());
        if (existing.isPresent()) throw new BadRequestException.BookBadRequestException("Book with such params already exists", existing.get());
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

    @Override
    public List<Book> getByFilter(
            Optional<String> title,
            Optional<String> genreName,
            Optional<String> authorFirstName,
            Optional<String> authorLastName,
            Optional<String> authorMidName,
            int offset, int pageSize
    ) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT b FROM Book b");

        if (Stream.of(authorFirstName, authorLastName, authorMidName).anyMatch(Optional::isPresent))
            queryBuilder.append(" LEFT JOIN b.author a");

        if (Stream.of(genreName).anyMatch(Optional::isPresent))
            queryBuilder.append(" LEFT JOIN b.genres g");

        if (Stream.of(title, genreName, authorFirstName, authorLastName, authorMidName).anyMatch(Optional::isPresent)) {
            queryBuilder.append(" WHERE ");

            List<String> conditions = new ArrayList<>();

            if (title.isPresent()) conditions.add("b.title = :title");
            if (genreName.isPresent()) conditions.add("g.name = :genreName");
            if (authorFirstName.isPresent()) conditions.add("a.firstName = :authorFirstName");
            if (authorLastName.isPresent()) conditions.add("a.lastName = :authorLastName");
            if (authorMidName.isPresent()) conditions.add("a.midName = :authorMidName");

            queryBuilder.append(String.join(" AND ", conditions));
        }

        queryBuilder.append(" ORDER BY b.id ASC");

        TypedQuery<Book> query = entityManager.createQuery(queryBuilder.toString(), Book.class);

        title.ifPresent(s -> query.setParameter("title", s));
        genreName.ifPresent(s -> query.setParameter("genreName", s));
        authorFirstName.ifPresent(s -> query.setParameter("authorFirstName", s));
        authorLastName.ifPresent(s -> query.setParameter("authorLastName", s));
        authorMidName.ifPresent(s -> query.setParameter("authorMidName", s));

        return query
                .setMaxResults(pageSize)
                .setFirstResult(offset)
                .getResultList();
    }
}
