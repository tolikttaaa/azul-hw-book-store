package org.ttaaa.backendhw.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    @NotEmpty
    @Size(max = 256)
    private String title;

    @Positive
    private Double price = null;

    @NotNull
    private UUID authorId;

    private Set<UUID> genreIds;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TitleDto {
        @NotEmpty
        @Size(max = 256)
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceDto {
        @Positive
        private Double price = null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorIdDto {
        @NotNull
        private UUID authorId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenreIdsDto {
        private Set<UUID> genreIds;
    }
}
