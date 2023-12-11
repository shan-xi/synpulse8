package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.exceptions.ExchangeRateAPIRuntimeException;
import com.synpulse8.ebanking.transaction.services.dto.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeRateServiceImplUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RedissonClient redissonClient;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExchangeRatesFromCache_CacheHit_ReturnsExchangeRates() {
        // Arrange
        var currency = "USD";
        var exchangeRateResponse = new ExchangeRateResponse(
                "success",
                "https://www.exchangerate-api.com/docs",
                "https://www.exchangerate-api.com/terms",
                1702080001L,
                "Sat, 09 Dec 2023 00:00:01 +0000",
                Instant.now().plusSeconds(3600).getEpochSecond(),
                "",
                currency,
                Collections.singletonMap("EUR", 0.85)
        );
        var bucketMock = mock(RBucket.class);
        when(redissonClient.getBucket(anyString())).thenReturn(bucketMock);
        when(bucketMock.get()).thenReturn(exchangeRateResponse);

        // Act
        var actualResult = exchangeRateService.getExchangeRatesFromCache(currency);

        // Assert
        assertTrue(actualResult.isPresent());
        assertEquals(exchangeRateResponse.conversionRates(), actualResult.get());

        // Verify get data from redis cache 1 time
        verify(redissonClient, times(1)).getBucket(anyString());
        verify(bucketMock, times(1)).get();
    }

    @Test
    void getExchangeRatesFromCache_CacheMiss_ReturnsEmpty() {
        // Arrange
        var currency = "USD";
        var bucketMock = mock(RBucket.class);
        when(redissonClient.getBucket(anyString())).thenReturn(bucketMock);
        when(bucketMock.get()).thenReturn(null);

        // Act
        var actualResult = exchangeRateService.getExchangeRatesFromCache(currency);

        // Assert
        assertTrue(actualResult.isEmpty());

        // Verify get data from redis cache 1 time
        verify(redissonClient, times(1)).getBucket(anyString());
        verify(bucketMock, times(1)).get();
    }

    @Test
    void fetchAndCacheExchangeRates_SuccessfulResponse_ReturnsRates() {
        // Arrange
        var currency = "USD";
        var apiUrl = "https://v6.exchangerate-api.com/v6/{apiKey}/latest/{currency}";
        var exchangeRateResponse = new ExchangeRateResponse(
                "success",
                "https://www.exchangerate-api.com/docs",
                "https://www.exchangerate-api.com/terms",
                1702080001L,
                "Sat, 09 Dec 2023 00:00:01 +0000",
                Instant.now().plusSeconds(3600).getEpochSecond(),
                "",
                currency,
                Collections.singletonMap("USD", 1.0)
        );
        var bucketMock = mock(RBucket.class);
        when(restTemplate.getForObject(apiUrl, ExchangeRateResponse.class, null, currency))
                .thenReturn(exchangeRateResponse);
        when(redissonClient.getBucket(any())).thenReturn(bucketMock);

        // Act
        var actualRates = exchangeRateService.fetchAndCacheExchangeRates(currency);

        // Assert
        assertEquals(1.0, actualRates.get(currency));

        // Verify get data from redis cache 1 time
        verify(bucketMock).set(exchangeRateResponse);
        verify(bucketMock).expire(Instant.ofEpochSecond(exchangeRateResponse.timeNextUpdateUnix()));
    }

    @Test
    void fetchAndCacheExchangeRates_UnsuccessfulResponse_ThrowsException() {
        // Arrange
        var currency = "USD";
        var apiUrl = "https://v6.exchangerate-api.com/v6/{apiKey}/latest/{currency}";
        var exchangeRateResponse = new ExchangeRateResponse(
                "error",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(restTemplate.getForObject(apiUrl, ExchangeRateResponse.class, null, currency))
                .thenReturn(exchangeRateResponse);

        // Assert
        assertThrows(ExchangeRateAPIRuntimeException.class,
                () -> exchangeRateService.fetchAndCacheExchangeRates(currency));
    }
}
