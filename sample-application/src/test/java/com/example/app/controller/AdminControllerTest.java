package com.example.app.controller;

import com.example.app.entities.ApplicationUser;
import com.example.app.enums.Roles;
import com.example.app.repository.ApplicationUserRepository;
import com.example.securitystarter.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static String adminToken;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ApplicationUser user = ApplicationUser.builder()
            .password("password")
            .roles(List.of(Roles.ADMIN))
            .username("user@gmail.com")
            .build();

        ApplicationUser user1 = ApplicationUser.builder()
            .password("password")
            .roles(List.of(Roles.USER))
            .username("user1@gmail.com")
            .build();

        applicationUserRepository.saveAll(List.of(user, user1));

        adminToken = jwtService.generateToken(user);

    }

    @Test
    void getAllUsers() throws Exception {

        mockMvc.perform(get("/api/admin/users")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", hasSize(2)));

    }
}
