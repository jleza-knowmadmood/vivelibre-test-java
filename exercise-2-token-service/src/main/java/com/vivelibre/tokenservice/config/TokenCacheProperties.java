package com.vivelibre.tokenservice.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenCacheProperties {

    private long ttlSeconds = 300;
}
