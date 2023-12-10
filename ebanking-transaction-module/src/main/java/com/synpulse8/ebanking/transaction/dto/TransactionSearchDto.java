package com.synpulse8.ebanking.transaction.dto;

import com.synpulse8.ebanking.enums.Currency;

import java.time.LocalDate;

public record TransactionSearchDto(
        String accountUid,
        LocalDate startDate,
        LocalDate endDate,
        Integer pageNumber,
        Integer pageSize,
        Currency baseCurrency) {
}
