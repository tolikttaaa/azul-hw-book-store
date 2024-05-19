package org.ttaaa.backendhw.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfoDto {
    private final int status;
    private final String message;
    private final Object data;
    private String stackTrace;
    private List<ValidationError> errors;

    private record ValidationError(String field, String message) {}

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) errors = new ArrayList<>();

        errors.add(new ValidationError(field, message));
    }
}
