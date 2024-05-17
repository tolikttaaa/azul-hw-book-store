package org.ttaaa.backendhw.models.entity;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Book {
    private UUID id;

    private String title;

    private Double price = null;

    private Author author;

    private List<Genre> genres;
}
