package org.ttaaa.backendhw.models;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.ttaaa.backendhw.models.entity.Author;
import org.ttaaa.backendhw.models.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class BookSpecifications {
    public static Specification<Book> getFilteredBooks(BookFilterParam params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getTitle() != null) {
                predicates.add(criteriaBuilder.equal(root.get("title").as(String.class), params.getTitle()));
            }

            if (params.getAuthor() != null) {
                predicates.add(criteriaBuilder.equal(root.get("author").as(Author.class), params.getAuthor()));
            }

            if (params.getGenre() != null) {
                predicates.add(criteriaBuilder.literal(params.getGenre().getId()).in(root.join("genres").get("id")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
