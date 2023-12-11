package com.synpulse8.ebanking.transaction.controllers;

import com.synpulse8.ebanking.enums.Currency;
import com.synpulse8.ebanking.enums.Status;
import com.synpulse8.ebanking.response.dto.ResponseDto;
import com.synpulse8.ebanking.transaction.dto.TransactionListRes;
import com.synpulse8.ebanking.transaction.dto.TransactionSearchDto;
import com.synpulse8.ebanking.transaction.services.TransactionService;
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
    public ResponseEntity<ResponseDto<TransactionListRes>> getTransactionList(
            LocalDate startDate,
            LocalDate endDate,
            Integer page,
            Integer size,
            Currency baseCurrency) {
        var res = transactionService.getTransactionList(new TransactionSearchDto(startDate, endDate, page, size, baseCurrency));
        return ResponseEntity.ok().body(
                new ResponseDto<>(
                        Status.SUCCESS,
                        res
                )
        );
    }
}
