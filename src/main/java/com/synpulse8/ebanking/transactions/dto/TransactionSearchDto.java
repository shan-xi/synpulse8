package com.synpulse8.ebanking.transactions.dto;

import java.time.LocalDate;

public record TransactionSearchDto(
        String accountId,
        LocalDate startDate,
        LocalDate endDate,
        Integer pageNumber,
        Integer pageSize) {
}