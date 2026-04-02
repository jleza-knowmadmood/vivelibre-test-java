package com.vivelibre.tokenservice.controller;

import com.vivelibre.tokenservice.dto.RequestMetricsResponse;
import com.vivelibre.tokenservice.service.RequestMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@Tag(name = "Metrics", description = "Operations related to in-memory request metrics")
public class MetricsController {

    private final RequestMetricsService requestMetricsService;

    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    @Operation(summary = "Get request metrics", description = "Returns the number of processed requests kept in memory")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public RequestMetricsResponse getMetrics() {
        return new RequestMetricsResponse(requestMetricsService.getProcessedRequests());
    }
}
