package org.ttaaa.backendhw.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.ttaaa.backendhw.model.dto.ErrorInfoDto;

import java.util.Objects;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TRACE = "trace";

    @Value("${error.response.log.trace:false}")
    private boolean printStackTrace;

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleBadRequestException(ConstraintViolationException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders ignoredHeaders,
            HttpStatusCode status,
            WebRequest ignoredRequest
    ) {
        ErrorInfoDto errorInfo = new ErrorInfoDto(
                status.value(),
                "Validation error. Check 'errors' field for details.",
                null
        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorInfo.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }

        return ResponseEntity.unprocessableEntity().body(errorInfo);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            RuntimeException exception,
            WebRequest request
    ){
        log.error("Unknown error occurred", exception);
        return buildErrorResponse(
                exception,
                "Unknown error occurred",
                null,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        return buildErrorResponse(exception, statusCode, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            BadRequestException exception,
            HttpStatusCode httpStatusCode,
            WebRequest request
    ) {
        return buildErrorResponse(exception, exception.getMessage(), exception.getData(), httpStatusCode, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatusCode httpStatusCode,
            WebRequest request
    ) {
        return buildErrorResponse(exception, exception.getMessage(), null, httpStatusCode, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            Object data,
            HttpStatusCode httpStatusCode,
            WebRequest request
    ) {
        ErrorInfoDto errorDto = new ErrorInfoDto(httpStatusCode.value(), message, data);

        if (isTraceOn(request) && printStackTrace) errorDto.setStackTrace(ExceptionUtils.getStackTrace(exception));

        return ResponseEntity.status(httpStatusCode).body(errorDto);
    }

    private boolean isTraceOn(WebRequest request) {
        String [] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }
}
