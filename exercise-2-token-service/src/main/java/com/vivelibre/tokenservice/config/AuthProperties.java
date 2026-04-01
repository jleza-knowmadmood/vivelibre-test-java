package com.vivelibre.tokenservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "external-auth")
@Component
@Getter
@Setter
public class AuthProperties {
    private String baseUrl;
    private String username;
    private String password;
}
