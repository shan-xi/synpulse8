package com.synpulse8.ebanking.test.auth.dto;

import com.synpulse8.ebanking.Currency;

public record CreateAccountReq(
        String uid,
        Currency currency,
        String name,
        String password) {
}
