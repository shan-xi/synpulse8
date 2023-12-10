package com.synpulse8.ebanking.test.auth.dto;

import com.synpulse8.ebanking.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateAccountReq(
        @Schema(description = "User's Identity", example = "P-0123456789")
        String uid,
        @Schema(description = "Currency", example = "CHF")
        Currency currency,
        @Schema(description = "Account IBAN", example = "CH93-0000-0000-0000-0000-0")
        String iban,
        @Schema(description = "Account name", example = "spin")
        String name,
        @Schema(description = "Account password", example = "12345")
        String password) {
}
