package org.ttaaa.backendhw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ttaaa.backendhw.models.entity.Author;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    private String firstName;
    private String lastName;
    private String midName;

    public AuthorDto(Author author) {
        this(author.getFirstName(), author.getLastName(), author.getMidName());
    }

    public Author toEntity() {
        return toEntity(UUID.randomUUID());
    }

    public Author toEntity(UUID id) {
        return new Author(id, firstName, lastName, midName);
    }

    public static AuthorDto fromEntity(Author author) {
        return new AuthorDto(author);
    }
}
