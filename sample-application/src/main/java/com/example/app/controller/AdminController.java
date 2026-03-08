
package com.example.app.controller;

import com.example.app.dtos.ApplicationUserDto;
import com.example.app.dtos.PaginatedResponse;
import com.example.app.services.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ApplicationUserService applicationUserService;

    @GetMapping("/users")
    public ResponseEntity<PaginatedResponse<ApplicationUserDto>> getAllUsers(
        @RequestParam(defaultValue = "0", value = "page") int page,
        @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(applicationUserService.getAllUsers(page, size));
    }
}
