package com.pfelink.monolith.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfelink.monolith.application.auth.dto.request.LoginRequest;
import com.pfelink.monolith.application.auth.dto.request.RegisterStudentRequest;
import com.pfelink.monolith.infrastructure.persistence.auth.SpringDataUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthenticationE2E {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Test
    void studentRegistrationAndLoginFlow() throws Exception {
        // 1. Register Student
        RegisterStudentRequest registerReq = new RegisterStudentRequest(
            "student@pfe.tn", "Password123!", "E2E Student",
            "55667788", "12345678", "http://card.url", null, "FAC-001"
        );

        mockMvc.perform(post("/api/auth/signup/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@pfe.tn"));

        assertTrue(userRepository.existsByEmail("student@pfe.tn"));

        // 2. Login (Should work even if pending, or I might need to activate it)
        // For simplicity, let's assume login returns 200 but might have restricted access
        LoginRequest loginReq = new LoginRequest("student@pfe.tn", "Password123!");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("access_token"))
                .andExpect(cookie().exists("refresh_token"));
    }
}
