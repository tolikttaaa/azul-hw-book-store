package org.ttaaa.backendhw.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private UUID id;

    private String firstName;

    private String lastName;

    private String midName;
}
