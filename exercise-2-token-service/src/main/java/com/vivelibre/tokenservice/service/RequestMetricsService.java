package com.vivelibre.tokenservice.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class RequestMetricsService {

    private final AtomicInteger processedRequests = new AtomicInteger();

    public int incrementProcessedRequests() {
        return processedRequests.incrementAndGet();
    }

    public int getProcessedRequests() {
        return processedRequests.get();
    }
}
