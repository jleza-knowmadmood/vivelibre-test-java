package com.vivelibre.tokenservice.service;

import static java.util.Objects.isNull;

import com.vivelibre.tokenservice.client.AuthClient;
import com.vivelibre.tokenservice.dto.TokenResponse;
import com.vivelibre.tokenservice.exception.InvalidTokenException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final AuthClient authClient;

    public TokenResponse getToken() {
        log.info("Requesting token from external auth service");
        String token = authClient.requestToken();

        if (isNull(token) || token.isBlank()) {
            log.warn("Received empty or invalid token from external auth service");
            throw new InvalidTokenException("Token is empty or invalid");
        }

        TokenResponse response = new TokenResponse(token, Instant.now().truncatedTo(ChronoUnit.SECONDS));
        log.info("Token retrieved successfully");
        return response;
    }
}
