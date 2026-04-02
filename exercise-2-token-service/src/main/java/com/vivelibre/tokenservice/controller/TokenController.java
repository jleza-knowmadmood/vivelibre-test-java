package com.vivelibre.tokenservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.vivelibre.tokenservice.dto.TokenResponse;
import com.vivelibre.tokenservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
@Tag(name = "Token", description = "Operations related to token retrieval")
public class TokenController {

    private final TokenService tokenService;

    @GetMapping
    @Operation(summary = "Get token", description = "Retrieves a token from the external auth service and returns it with the current timestamp")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token retrieved successfully"),
            @ApiResponse(responseCode = "502", description = "External service error or invalid token")
    })
    public TokenResponse getToken() {
        return tokenService.getToken();
    }
}
