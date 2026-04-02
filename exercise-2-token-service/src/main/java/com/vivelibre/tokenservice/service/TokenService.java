package com.vivelibre.tokenservice.service;

import com.vivelibre.tokenservice.dto.TokenResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final CachedTokenProvider cachedTokenProvider;

    public TokenResponse getToken() {
        String token = cachedTokenProvider.getToken();
        TokenResponse response = new TokenResponse(token, Instant.now().truncatedTo(ChronoUnit.SECONDS));
        log.info("Returning token response");
        return response;
    }
}
