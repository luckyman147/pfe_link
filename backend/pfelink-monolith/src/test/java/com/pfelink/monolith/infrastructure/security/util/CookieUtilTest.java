package com.pfelink.monolith.infrastructure.security.util;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CookieUtilTest {

    private CookieUtil cookieUtil;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil();
        response = mock(HttpServletResponse.class);
    }

    @Test
    void setTokenCookies_ShouldAddCorrectHeaders() {
        // Arrange
        String accessToken = "test-access";
        String refreshToken = "test-refresh";

        // Act
        cookieUtil.setTokenCookies(response, accessToken, refreshToken);

        // Assert
        ArgumentCaptor<String> headerNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> headerValueCaptor = ArgumentCaptor.forClass(String.class);
        
        verify(response, times(2)).addHeader(headerNameCaptor.capture(), headerValueCaptor.capture());

        List<String> values = headerValueCaptor.getAllValues();
        
        assertTrue(values.stream().anyMatch(v -> v.contains("access_token=test-access") && v.contains("HttpOnly") && v.contains("SameSite=Strict")));
        assertTrue(values.stream().anyMatch(v -> v.contains("refresh_token=test-refresh") && v.contains("HttpOnly") && v.contains("SameSite=Strict")));
    }

    @Test
    void clearTokenCookies_ShouldSetMaxAgeToZero() {
        // Act
        cookieUtil.clearTokenCookies(response);

        // Assert
        ArgumentCaptor<String> headerValueCaptor = ArgumentCaptor.forClass(String.class);
        verify(response, times(2)).addHeader(eq("Set-Cookie"), headerValueCaptor.capture());

        List<String> values = headerValueCaptor.getAllValues();
        assertTrue(values.stream().anyMatch(v -> v.contains("access_token=") && v.contains("Max-Age=0")));
        assertTrue(values.stream().anyMatch(v -> v.contains("refresh_token=") && v.contains("Max-Age=0")));
    }
}
