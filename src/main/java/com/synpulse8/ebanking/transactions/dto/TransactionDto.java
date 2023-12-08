package com.synpulse8.ebanking.transactions.dto;

import com.synpulse8.ebanking.enums.Currency;

import java.util.Date;

public record TransactionDto(
        String transactionId,
        Currency currency,
        Double amount,
        String iban,
        Date valueDate,
        String description,
        String accountUid) {
}
