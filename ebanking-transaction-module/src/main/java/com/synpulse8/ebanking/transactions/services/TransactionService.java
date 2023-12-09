package com.synpulse8.ebanking.transactions.services;

import com.synpulse8.ebanking.transactions.dto.TransactionListRes;
import com.synpulse8.ebanking.transactions.dto.TransactionSearchDto;

public interface TransactionService {
    TransactionListRes getTransactionList(TransactionSearchDto transactionSearchDto);
}
