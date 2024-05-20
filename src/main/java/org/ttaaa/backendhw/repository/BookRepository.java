package org.ttaaa.backendhw.repository;

import java.util.List;
import java.util.Optional;
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
    List<Book> getByFilter(
            Optional<String> title,
            Optional<String> genreName,
            Optional<String> authorFirstName,
            Optional<String> authorLastName,
            Optional<String> authorMidName,
            int offset, int pageSize
    );
}
