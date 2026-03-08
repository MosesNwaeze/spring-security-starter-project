package com.example.securitystarter.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class AuthenticationException extends RuntimeException {

    private final int status;
    private final String path;
    private final LocalDateTime now;
    public AuthenticationException(String message, int status, String path, LocalDateTime now) {
        super(message);
        this.status = status;
        this.path = path;
        this.now = now;
    }
}
