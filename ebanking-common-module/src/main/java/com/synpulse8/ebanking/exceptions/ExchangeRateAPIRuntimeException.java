package com.synpulse8.ebanking.exceptions;

import com.synpulse8.ebanking.exceptions.base.SynpulseSystemRuntimeException;

public class ExchangeRateAPIRuntimeException extends SynpulseSystemRuntimeException {

    private static final String CODE = "EXCHANGE_RATE_API_ERROR";

    private String message = "EXCHANGE_RATE_API_ERROR";

    public ExchangeRateAPIRuntimeException() {
    }

    public ExchangeRateAPIRuntimeException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public String getMessage() {
        return message;
    }

}