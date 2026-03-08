package com.example.securitystarter.model;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface SecureUserService {
    Optional<SecureUser> findByUserName(String userName);
}

