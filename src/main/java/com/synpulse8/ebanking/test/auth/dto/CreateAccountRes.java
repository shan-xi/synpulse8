package com.synpulse8.ebanking.test.auth.dto;

public record CreateAccountRes(
        String email,
        String encodePassword) {
}
