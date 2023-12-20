package com.synpulse8.ebanking.dao.transaction.dto;

import com.synpulse8.ebanking.enums.BalanceChange;
import com.synpulse8.ebanking.enums.Currency;

import java.time.LocalDate;

public record TransactionDto(
        String transactionId,
        Currency currency,
        Double amount,
        BalanceChange balanceChange,
        String iban,
        LocalDate valueDate,
        String description,
        Long accountId) {
}
