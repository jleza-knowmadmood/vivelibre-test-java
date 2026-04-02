package com.vivelibre.tokenservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @ConfigurationProperties(prefix = "token-cache")
    public TokenCacheProperties tokenCacheProperties() {
        return new TokenCacheProperties();
    }

    @Bean
    public CacheManager cacheManager(TokenCacheProperties tokenCacheProperties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("token");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(tokenCacheProperties.getTtlSeconds())));
        return cacheManager;
    }
}
