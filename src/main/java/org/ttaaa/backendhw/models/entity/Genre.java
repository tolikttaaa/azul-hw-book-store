package org.ttaaa.backendhw.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private UUID id;

    private String name;
}
