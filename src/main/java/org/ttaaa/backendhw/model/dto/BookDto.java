package org.ttaaa.backendhw.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String title;
    private Double price = null;
    private UUID authorId;
    private Set<UUID> genreIds;
}
