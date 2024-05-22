package org.ttaaa.backendhw.repository;

import org.ttaaa.backendhw.model.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookFilterRepository {
    List<Book> getByFilter(
            Optional<String> title,
            Optional<String> genreName,
            Optional<String> authorFirstName,
            Optional<String> authorLastName,
            Optional<String> authorMidName,
            int offset, int pageSize
    );
}
