package com.vivelibre.tokenservice.service;

import com.vivelibre.tokenservice.client.AuthClient;
import com.vivelibre.tokenservice.exception.InvalidTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachedTokenProviderTest {

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private CachedTokenProvider cachedTokenProvider;

    @Test
    void returnsTokenFromExternalService() {
        when(authClient.requestToken()).thenReturn("sample-token");

        assertThat(cachedTokenProvider.getToken()).isEqualTo("sample-token");
    }

    @Test
    void rejectsBlankToken() {
        when(authClient.requestToken()).thenReturn(" ");

        assertThatThrownBy(cachedTokenProvider::getToken)
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Token is empty or invalid");
    }
}
