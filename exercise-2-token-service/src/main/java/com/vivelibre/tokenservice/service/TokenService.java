package com.vivelibre.tokenservice.service;

import static java.util.Objects.isNull;

import com.vivelibre.tokenservice.client.AuthClient;
import com.vivelibre.tokenservice.dto.TokenResponse;
import com.vivelibre.tokenservice.exception.InvalidTokenException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AuthClient authClient;

    public TokenResponse getToken() {
        String token = authClient.requestToken();

        if (isNull(token) || token.isBlank()) {
            throw new InvalidTokenException("Token is empty or invalid");
        }

        return new TokenResponse(token, Instant.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
