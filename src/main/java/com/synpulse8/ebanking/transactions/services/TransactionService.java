package com.synpulse8.ebanking.transactions.services;

import com.synpulse8.ebanking.transactions.dto.TransactionListRes;

import java.time.LocalDate;

public interface TransactionService {
    TransactionListRes getTransactionList(String accountId, LocalDate month, Integer pageNumber, Integer pageSize);
}
