package com.example.app.controller;

import com.example.app.dtos.ApplicationUserDto;
import com.example.app.dtos.AuthResponseDto;
import com.example.app.services.ApplicationUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ApplicationUserService applicationUserService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<ApplicationUserDto> register(@Valid @RequestBody ApplicationUserDto user) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(applicationUserService.registerUser(user));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody ApplicationUserDto user) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(applicationUserService.login(user, authenticationManager));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody AuthResponseDto authResponseDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(applicationUserService.refreshToken(authResponseDto, userDetailsService));
    }
}
