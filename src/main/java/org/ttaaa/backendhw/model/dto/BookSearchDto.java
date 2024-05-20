package org.ttaaa.backendhw.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDto {
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String authorMidName;
    private String genreName;
}
