package com.vivelibre.tokenservice.dto;

import java.time.Instant;

public record TokenResponse(
        String token,
        Instant timestamp
) {}
