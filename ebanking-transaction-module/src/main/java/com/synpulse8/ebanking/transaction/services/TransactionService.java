package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.transaction.dto.TransactionListRes;
import com.synpulse8.ebanking.transaction.dto.TransactionSearchDto;

public interface TransactionService {
    TransactionListRes getTransactionList(TransactionSearchDto transactionSearchDto);
}
