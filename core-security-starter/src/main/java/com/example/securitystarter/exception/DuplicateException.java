package com.example.securitystarter.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class DuplicateException extends RuntimeException {

    private final int status;
    private final String path;
    private final LocalDateTime now;

    public DuplicateException(String message, int status, String path, LocalDateTime now) {
        super(message);
        this.status = status;
        this.path = path;
        this.now = now;
    }
}
