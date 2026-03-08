package com.example.securitystarter.model;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.List;

public interface SecureUser {
    String getUsername();

    String getPassword(); // Encrypted

    List<?> getRoles();

    Long getUserId();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

}
