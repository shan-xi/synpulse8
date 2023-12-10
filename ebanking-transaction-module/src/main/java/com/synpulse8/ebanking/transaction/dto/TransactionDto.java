package com.synpulse8.ebanking.transaction.dto;

import java.util.Date;

public record TransactionDto(
        String transactionId,
        Double amount,
        String iban,
        Date valueDate,
        String description,
        String accountUid) {
}
