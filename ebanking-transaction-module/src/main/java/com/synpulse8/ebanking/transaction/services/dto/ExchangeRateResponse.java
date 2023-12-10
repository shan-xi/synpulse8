package com.synpulse8.ebanking.transaction.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ExchangeRateResponse(
        String result,
        String documentation,
        @JsonProperty(value = "terms_of_use")
        String termsOfUse,
        @JsonProperty(value = "time_last_update_unix")
        Long timeLastUpdateUnix,
        @JsonProperty(value = "time_last_update_utc")
        String timeLastUpdateUtc,
        @JsonProperty(value = "time_next_update_unix")
        Long timeNextUpdateUnix,
        @JsonProperty(value = "time_next_update_utc")
        String timeNextUpdateUtc,
        @JsonProperty(value = "base_code")
        String baseCode,
        @JsonProperty(value = "conversion_rates")
        Map<String, Double> conversionRates) {
}
