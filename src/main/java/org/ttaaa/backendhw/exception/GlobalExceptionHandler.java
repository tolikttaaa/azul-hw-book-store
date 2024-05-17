package org.ttaaa.backendhw.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.ttaaa.backendhw.models.dto.ErrorInfoDto;

import java.util.Objects;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TRACE = "trace";

    @Value("${error.response.log.trace:false}")
    private boolean printStackTrace;

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException exception, WebRequest request) {
        log.warn("Failed to find the requested element", exception);
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException exception, WebRequest request) {
        log.warn("Failed to process the request", exception);
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
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
        return buildErrorResponse(exception, exception.getMessage(), httpStatusCode, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatusCode httpStatusCode,
            WebRequest request
    ) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatusCode, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatusCode httpStatusCode,
            WebRequest request
    ) {
        ErrorInfoDto errorDto = new ErrorInfoDto(httpStatusCode.value(), message);

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
