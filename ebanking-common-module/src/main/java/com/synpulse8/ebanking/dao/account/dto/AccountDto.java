package com.synpulse8.ebanking.dao.account.dto;

import com.synpulse8.ebanking.enums.Currency;

public record AccountDto(
        Long id,
        String uid,
        Currency currency,
        String iban,
        Long clientId) {
}
