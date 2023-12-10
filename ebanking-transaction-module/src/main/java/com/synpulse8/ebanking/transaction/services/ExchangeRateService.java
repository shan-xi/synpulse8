package com.synpulse8.ebanking.transaction.services;

import java.util.Map;

public interface ExchangeRateService {
    Map<String, Double> getExchangeRates(String currency);
}
