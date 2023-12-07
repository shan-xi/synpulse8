package com.synpulse8.ebanking.transactions.controllers;

import com.synpulse8.ebanking.transactions.dto.TransactionListRes;
import com.synpulse8.ebanking.transactions.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class TransactionControllerImpl implements TransactionController {
    private final TransactionService transactionService;

    public TransactionControllerImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<TransactionListRes> getTransactionList(
            String accountUid,
            LocalDate month,
            Integer page,
            Integer size) {
        var res = transactionService.getTransactionList(accountUid, month, page, size);
        return ResponseEntity.ok().body(res);
    }
}
