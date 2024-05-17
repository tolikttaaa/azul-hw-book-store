package org.ttaaa.backendhw.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.ttaaa.backendhw.models.dto.AuthorDto;
import org.ttaaa.backendhw.models.dto.GenreDto;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String resourceName, UUID resourceId) {
        super(String.format("The requested %s with id='%s' was not found", resourceName, resourceId));
    }

    public NotFoundException(String resourceName, Object resource) {
        super(String.format("The requested %s was not found, %s: %s", resourceName, resourceName, resource.toString()));
    }

    public static class GenreNotFoundException extends NotFoundException {
        public GenreNotFoundException(UUID id) {
            super("genre", id);
        }

        public GenreNotFoundException(GenreDto genreDto) {
            super("genre", genreDto);
        }
    }

    public static class AuthorNotFoundException extends NotFoundException {
        public AuthorNotFoundException(UUID id) {
            super("author", id);
        }

        public AuthorNotFoundException(AuthorDto authorDto) {
            super("author", authorDto);
        }
    }

    public static class BookNotFoundException extends NotFoundException {
        public BookNotFoundException(UUID id) {
            super("book", id);
        }
    }
}
