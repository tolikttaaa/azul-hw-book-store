package org.ttaaa.backendhw.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.model.entity.Genre;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private final Object data;

    public BadRequestException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public static class GenreBadRequestException extends BadRequestException {
        public GenreBadRequestException(String message, Genre entity) {
            super(message, entity);
        }
    }

    public static class AuthorBadRequestException extends BadRequestException {
        public AuthorBadRequestException(String message, Author entity) {
            super(message, entity);
        }
    }

    public static class BookBadRequestException extends BadRequestException {
        public BookBadRequestException(String message, Book entity) {
            super(message, entity);
        }
    }
}
