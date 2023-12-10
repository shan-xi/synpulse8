package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.transaction.services.dto.ExchangeRateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RedissonClient redissonClient;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Test
    void getExchangeRatesFromCache_CacheHit_ReturnsExchangeRates() {
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

        var result = exchangeRateService.getExchangeRatesFromCache(currency);

        assertTrue(result.isPresent());
        assertEquals(exchangeRateResponse.conversionRates(), result.get());

        verify(redissonClient, times(1)).getBucket(anyString());
        verify(bucketMock, times(1)).get();
    }

    @Test
    void getExchangeRatesFromCache_CacheMiss_ReturnsEmpty() {
        var currency = "USD";

        var bucketMock = mock(RBucket.class);
        when(redissonClient.getBucket(anyString())).thenReturn(bucketMock);
        when(bucketMock.get()).thenReturn(null);

        var result = exchangeRateService.getExchangeRatesFromCache(currency);

        assertTrue(result.isEmpty());

        verify(redissonClient, times(1)).getBucket(anyString());
        verify(bucketMock, times(1)).get();
    }
}
