package org.ttaaa.backendhw.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.repository.AbstractRepository;
import org.ttaaa.backendhw.repository.BookFilterRepository;

import java.util.*;
import java.util.stream.Stream;

@Repository
public class BookFilterRepositoryImpl extends AbstractRepository implements BookFilterRepository {
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
