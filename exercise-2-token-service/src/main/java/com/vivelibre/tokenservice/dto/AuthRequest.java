package com.vivelibre.tokenservice.dto;

public record AuthRequest(
        String username,
        String password
) {}
