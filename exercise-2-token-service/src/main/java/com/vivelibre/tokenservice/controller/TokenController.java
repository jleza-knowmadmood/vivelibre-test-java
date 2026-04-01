package com.vivelibre.tokenservice.controller;

import com.vivelibre.tokenservice.dto.TokenResponse;
import com.vivelibre.tokenservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping
    public TokenResponse getToken() {
        return tokenService.getToken();
    }
}
