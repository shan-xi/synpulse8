package com.synpulse8.ebanking.test.auth.dto;

public record CreateAccountReq(
        String name,
        String email,
        String password) {
}