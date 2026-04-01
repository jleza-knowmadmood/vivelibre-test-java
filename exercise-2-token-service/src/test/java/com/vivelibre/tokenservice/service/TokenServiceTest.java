package com.vivelibre.tokenservice.service;

import com.vivelibre.tokenservice.client.AuthClient;
import com.vivelibre.tokenservice.dto.TokenResponse;
import com.vivelibre.tokenservice.exception.InvalidTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void returnsTokenWithTimestampTruncatedToSeconds() {
        when(authClient.requestToken()).thenReturn("sample-token");

        TokenResponse response = tokenService.getToken();

        assertAll(
                () -> assertThat(response.token()).isEqualTo("sample-token"),
                () -> assertThat(response.timestamp()).isNotNull(),
                () -> assertThat(response.timestamp().getNano()).isZero()
        );
    }

    @Test
    void rejectsBlankToken() {
        when(authClient.requestToken()).thenReturn(" ");

        assertThatThrownBy(tokenService::getToken)
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Token is empty or invalid");
    }
}
