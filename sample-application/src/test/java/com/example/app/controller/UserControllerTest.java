package com.example.app.controller;

import com.example.app.entities.ApplicationUser;
import com.example.app.enums.Roles;
import com.example.app.repository.ApplicationUserRepository;
import com.example.securitystarter.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private JwtService jwtService;

    private String token;

    @BeforeEach
    void setUp() {

        ApplicationUser user = ApplicationUser.builder()
            .username("user@gmail.com")
            .password("password")
            .roles(List.of(Roles.ADMIN, Roles.USER))
            .build();

        applicationUserRepository.save(user);

        token = jwtService.generateToken(user);

    }

    @Test
    @DisplayName("Should return user details")
    void me() throws Exception {
        mockMvc.perform(get("/api/user/me")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("user@gmail.com"));
    }


}
