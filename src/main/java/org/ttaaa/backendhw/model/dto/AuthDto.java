package org.ttaaa.backendhw.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    @NotEmpty
    @Size(max = 256)
    private String username;

    @NotEmpty
    @Size(max = 256)
    private String password;
}
