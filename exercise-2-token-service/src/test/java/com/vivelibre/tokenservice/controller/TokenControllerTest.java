package com.vivelibre.tokenservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vivelibre.tokenservice.advice.GlobalExceptionHandler;
import com.vivelibre.tokenservice.dto.TokenResponse;
import com.vivelibre.tokenservice.exception.InvalidTokenException;
import com.vivelibre.tokenservice.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

    @Mock
    private TokenService tokenService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(new TokenController(tokenService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void returnsTokenResponse() throws Exception {
        when(tokenService.getToken()).thenReturn(
                new TokenResponse("sample-token", Instant.parse("2026-04-01T14:52:22Z"))
        );

        mockMvc.perform(get("/token"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.token").value("sample-token"),
                        jsonPath("$.timestamp").value("2026-04-01T14:52:22Z")
                );
    }

    @Test
    void returnsBadGatewayWhenTokenIsInvalid() throws Exception {
        when(tokenService.getToken()).thenThrow(new InvalidTokenException("Token is empty or invalid"));

        mockMvc.perform(get("/token"))
                .andExpectAll(
                        status().isBadGateway(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.error").value("Token is empty or invalid")
                );
    }
}
