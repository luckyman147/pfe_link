package com.pfelink.monolith.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfelink.monolith.application.auth.command.login.LoginCommand;
import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.application.auth.dto.request.LoginRequest;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.infrastructure.security.util.CookieUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.shared.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false) // Disable security filters for component tests
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Dispatcher dispatcher;

    @MockBean
    private CookieUtil cookieUtil;

    @Test
    void login_ShouldSetCookies_WhenSuccessful() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("test@me.com", "password");
        AuthResponseDTO responseDTO = AuthResponseDTO.of(
            UUID.randomUUID(), "test@me.com", "Test User",
            UserRole.STUDENT, AccountStatus.ACTIVE, "atoken", "rtoken"
        );
        
        when(dispatcher.send(any(LoginCommand.class))).thenReturn(Result.success(responseDTO));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("atoken"));

        verify(cookieUtil).setTokenCookies(any(), eq("atoken"), eq("rtoken"));
    }

    @Test
    void logout_ShouldClearCookies() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));

        verify(cookieUtil).clearTokenCookies(any());
    }
}
