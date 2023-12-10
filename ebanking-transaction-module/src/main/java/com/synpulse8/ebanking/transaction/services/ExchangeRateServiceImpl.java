package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.exceptions.ExchangeRateAPIRuntimeException;
import com.synpulse8.ebanking.transaction.services.dto.ExchangeRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final RestTemplate restTemplate;
    private final RedissonClient redissonClient;

    @Value("${exchange-rate-api.key}")
    private String apiKey;

    public ExchangeRateServiceImpl(RestTemplate restTemplate,
                                   RedissonClient redissonClient) {
        this.restTemplate = restTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public Map<String, Double> getExchangeRates(String currency) {
        return getExchangeRatesFromCache(currency)
                .orElseGet(() -> fetchAndCacheExchangeRates(currency));
    }

    Optional<Map<String, Double>> getExchangeRatesFromCache(String currency) {
        RBucket<ExchangeRateResponse> bucket = redissonClient.getBucket(currency + "-exchangeRateResponse");
        var exchangeRateResponse = bucket.get();

        return Optional.ofNullable(exchangeRateResponse)
                .filter(resp -> Instant.now().isBefore(Instant.ofEpochSecond(resp.timeNextUpdateUnix())))
                .map(ExchangeRateResponse::conversionRates);
    }

    Map<String, Double> fetchAndCacheExchangeRates(String currency) {
        var apiUrl = "https://v6.exchangerate-api.com/v6/{apiKey}/latest/{currency}";
        var exchangeRateResponse = restTemplate.getForObject(apiUrl, ExchangeRateResponse.class, apiKey, currency);

        return Optional.ofNullable(exchangeRateResponse)
                .filter(resp -> resp.result().equals("success"))
                .map(resp -> {
                    var bucket = redissonClient.getBucket(currency + "-exchangeRateResponse");
                    bucket.set(exchangeRateResponse);
                    bucket.expire(Instant.ofEpochSecond(exchangeRateResponse.timeNextUpdateUnix()));
                    return resp.conversionRates();
                })
                .orElseThrow(() -> {
                    var errorMsg = "Unknown Error!";
                    if (Objects.nonNull(exchangeRateResponse)) {
                        errorMsg = exchangeRateResponse.result();
                    }
                    return new ExchangeRateAPIRuntimeException("Exchange Rate API error: " + errorMsg);
                });
    }
}
