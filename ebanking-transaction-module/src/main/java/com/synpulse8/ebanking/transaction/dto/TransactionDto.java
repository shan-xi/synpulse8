package com.synpulse8.ebanking.transaction.dto;

import com.synpulse8.ebanking.enums.BalanceChange;

import java.time.LocalDate;

public record TransactionDto(
        String transactionId,
        Double amount,
        BalanceChange balanceChange,
        String iban,
        LocalDate valueDate,
        String description,
        String accountUid) {
}
