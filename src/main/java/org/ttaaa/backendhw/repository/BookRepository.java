package org.ttaaa.backendhw.repository;

import java.util.Set;
import java.util.UUID;

import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.model.entity.Genre;

public interface BookRepository extends EntityRepository<Book, UUID> {
    Book updateBookTitle(UUID id, String title);
    Book updateBookPrice(UUID id, Double price);
    Book updateBookAuthor(UUID id, Author author);
    Book updateBookGenres(UUID id, Set<Genre> genres);
}
