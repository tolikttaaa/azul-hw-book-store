package org.ttaaa.backendhw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ttaaa.backendhw.models.entity.Author;
import org.ttaaa.backendhw.models.entity.Genre;

@Data
@AllArgsConstructor
public class BookFilterParam {
    private String title;
    private Genre genre;
    private Author author;
}
