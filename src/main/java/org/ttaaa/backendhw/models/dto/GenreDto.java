package org.ttaaa.backendhw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ttaaa.backendhw.models.entity.Genre;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {
    private String name;

    public GenreDto(Genre genre) {
        this(genre.getName());
    }

    public Genre toEntity() {
        return toEntity(UUID.randomUUID());
    }

    public Genre toEntity(UUID id) {
        return new Genre(id, name);
    }

    public static GenreDto fromEntity(Genre genre) {
        return new GenreDto(genre);
    }
}
