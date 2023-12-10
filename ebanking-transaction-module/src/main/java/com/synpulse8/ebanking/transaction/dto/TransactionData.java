package com.synpulse8.ebanking.transaction.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class TransactionData {
    private String accountUid;
    private String amountWithCurrency;
    private String iban;
    private String valueDate;
    private String description;
}
