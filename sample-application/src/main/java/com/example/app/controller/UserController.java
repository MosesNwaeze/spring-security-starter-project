
package com.example.app.controller;

import com.example.app.dtos.ApplicationUserDto;
import com.example.app.services.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final ApplicationUserService applicationUserService;

    @GetMapping("/me")
    public ResponseEntity<ApplicationUserDto> me() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(applicationUserService.getUser());
    }

}
