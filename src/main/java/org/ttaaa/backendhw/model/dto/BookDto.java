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
}
