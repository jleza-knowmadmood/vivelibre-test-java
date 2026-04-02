package com.vivelibre.tokenservice.service;

import com.vivelibre.tokenservice.dto.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private CachedTokenProvider cachedTokenProvider;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void returnsTokenWithTimestampTruncatedToSeconds() {
        when(cachedTokenProvider.getToken()).thenReturn("sample-token");

        TokenResponse response = tokenService.getToken();

        assertAll(
                () -> assertThat(response.token()).isEqualTo("sample-token"),
                () -> assertThat(response.timestamp()).isNotNull(),
                () -> assertThat(response.timestamp().getNano()).isZero()
        );
    }
}
