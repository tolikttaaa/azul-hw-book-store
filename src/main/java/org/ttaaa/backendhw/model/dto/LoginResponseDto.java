package org.ttaaa.backendhw.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;

    private Date expiresAt;
}
