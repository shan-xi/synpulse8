package com.synpulse8.ebanking.test.auth.dto;

public record CreateAccountReq(
        String uid,
        String currency,
        String name,
        String password) {
}
