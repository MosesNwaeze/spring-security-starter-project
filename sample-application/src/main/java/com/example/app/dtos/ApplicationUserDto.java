package com.example.app.dtos;


import com.example.app.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties()
public class ApplicationUserDto {
    private Long userId;
    @NotBlank(message = "Username is required")
    private String username;
    private String password;
    @NotEmpty(message = "Roles are required")
    private List<Roles> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
