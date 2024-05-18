package org.ttaaa.backendhw.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    private String firstName;
    private String lastName;
    private String midName;
}
