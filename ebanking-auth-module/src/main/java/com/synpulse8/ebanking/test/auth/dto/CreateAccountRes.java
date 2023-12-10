package com.synpulse8.ebanking.test.auth.dto;


import com.synpulse8.ebanking.enums.Currency;

public record CreateAccountRes(
        String uid,
        Currency currency,
        String encodePassword) {
}
