package com.vivelibre.tokenservice.client;

import static java.util.Objects.isNull;

import com.vivelibre.tokenservice.config.AuthProperties;
import com.vivelibre.tokenservice.dto.AuthRequest;
import com.vivelibre.tokenservice.dto.AuthResponse;
import com.vivelibre.tokenservice.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestClient restClient;
    private final AuthProperties properties;

    public String requestToken() {
        try {
            AuthResponse response = restClient
                    .post()
                    .uri(properties.getBaseUrl() + "/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthRequest(
                            properties.getUsername(),
                            properties.getPassword()))
                    .retrieve()
                    .body(AuthResponse.class);

            if (isNull(response)) {
                throw new ExternalServiceException("Null response from external service");
            }

            return response.token();

        } catch (Exception ex) {
            throw new ExternalServiceException("Error calling external service: " + ex.getMessage(), ex);
        }
    }
}
