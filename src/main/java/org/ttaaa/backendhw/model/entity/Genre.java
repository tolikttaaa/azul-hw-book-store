package org.ttaaa.backendhw.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@Entity
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;
}
