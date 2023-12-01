package com.synpulse8.ebanking.transactions.controllers;

import com.synpulse8.ebanking.transactions.dto.TransactionListRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionControllerImpl implements TransactionController {

    @Override
    public ResponseEntity<TransactionListRes> getTransactionList(String account_id) {
        return null;
    }
}
