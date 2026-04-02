package com.vivelibre.tokenservice.service;

import static java.util.Objects.isNull;

import com.vivelibre.tokenservice.client.AuthClient;
import com.vivelibre.tokenservice.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CachedTokenProvider {

    private final AuthClient authClient;

    @Cacheable(value = "token", sync = true)
    public String getToken() {
        log.info("Requesting token from external auth service");
        String token = authClient.requestToken();

        if (isNull(token) || token.isBlank()) {
            log.warn("Received empty or invalid token from external auth service");
            throw new InvalidTokenException("Token is empty or invalid");
        }

        log.info("Token retrieved successfully");
        return token;
    }
}
