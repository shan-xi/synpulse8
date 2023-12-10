package com.synpulse8.ebanking.test.kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Transaction Record")
public record TransactionRecord(
        @Schema(description = "User's Identity", example = "P-0123456789")
        String accountUid,
        @Schema(description = "Money with currency", example = "CHF 100-")
        String amountWithCurrency,
        @Schema(description = "Account IBAN", example = "CH93-0000-0000-0000-0000-0")
        String iban,
        @Schema(description = "Value Date", example = "01-10-2020")
        String valueDate,
        @Schema(description = "Description", example = "Online payment CHF")
        String description) {
}
