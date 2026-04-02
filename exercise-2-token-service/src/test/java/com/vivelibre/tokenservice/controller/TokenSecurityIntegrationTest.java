package com.vivelibre.tokenservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vivelibre.tokenservice.dto.TokenResponse;
import com.vivelibre.tokenservice.service.RequestMetricsService;
import com.vivelibre.tokenservice.service.TokenService;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TokenSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private RequestMetricsService requestMetricsService;

    @Test
    void rejectsRequestWithoutBasicAuth() throws Exception {
        mockMvc.perform(get("/token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsMetricsRequestWithoutBasicAuth() throws Exception {
        mockMvc.perform(get("/metrics"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void allowsRequestWithValidBasicAuth() throws Exception {
        when(tokenService.getToken()).thenReturn(
                new TokenResponse("sample-token", Instant.parse("2026-04-01T14:52:22Z"))
        );

        mockMvc.perform(get("/token")
                        .with(httpBasic("token-user", "token-password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("sample-token"))
                .andExpect(jsonPath("$.timestamp").value("2026-04-01T14:52:22Z"));
    }

    @Test
    void returnsMetricsWithValidBasicAuth() throws Exception {
        when(requestMetricsService.getProcessedRequests()).thenReturn(5);

        mockMvc.perform(get("/metrics")
                        .with(httpBasic("token-user", "token-password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestsProcessed").value(5));
    }
}
