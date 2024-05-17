package org.ttaaa.backendhw.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.ttaaa.backendhw.models.dto.AuthorDto;
import org.ttaaa.backendhw.models.dto.GenreDto;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public static class GenreBadRequestException extends BadRequestException {
        public GenreBadRequestException(GenreDto genre) {
            super("Failed to create genre: " + genre.toString());
        }
    }

    public static class AuthorBadRequestException extends BadRequestException {
        public AuthorBadRequestException(AuthorDto author) {
            super("Failed to create author: " + author.toString());
        }
    }
}
