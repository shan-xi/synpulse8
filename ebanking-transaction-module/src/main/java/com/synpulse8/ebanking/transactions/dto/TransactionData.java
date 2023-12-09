package com.synpulse8.ebanking.transactions.dto;

import com.synpulse8.ebanking.enums.Currency;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class TransactionData {
    private Currency currency;
    private Double amount;
    private String iban;
    private Date valueDate;
    private String description;
    private String accountUid;
}
