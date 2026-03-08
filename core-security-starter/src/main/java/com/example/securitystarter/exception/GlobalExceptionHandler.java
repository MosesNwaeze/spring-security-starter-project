
package com.example.securitystarter.exception;

import com.example.securitystarter.model.ErrorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.FORBIDDEN.value())
            .timestamp(ex.getNow())
            .path(ex.getPath())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthenticationException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.UNAUTHORIZED.value())
            .timestamp(ex.getNow())
            .path(ex.getPath())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(ex.getMessage())
            .status(ex.getStatus())
            .timestamp(ex.getNow())
            .path(ex.getPath())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        ErrorResponse error = ErrorResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
