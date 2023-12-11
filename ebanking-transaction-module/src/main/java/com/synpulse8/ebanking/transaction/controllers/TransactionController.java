package com.synpulse8.ebanking.transaction.controllers;

import com.synpulse8.ebanking.enums.Currency;
import com.synpulse8.ebanking.response.dto.ResponseDto;
import com.synpulse8.ebanking.security.EbankingPrincipal;
import com.synpulse8.ebanking.transaction.dto.TransactionListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "E-Banking - Transaction", description = "Transaction management APIs")
@RequestMapping("/transactions")
public interface TransactionController {
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Query user transaction records",
            description = "Query user transaction records")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Query user transaction records success",
                    useReturnTypeSchema = true))
    @GetMapping
    ResponseEntity<ResponseDto<TransactionListRes>> getTransactionList(
            @AuthenticationPrincipal
            EbankingPrincipal ebankingPrincipal,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Schema(description = "Transaction start date", example = "2023-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
            LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Schema(description = "Transaction end date", example = "2023-01-31", requiredMode = Schema.RequiredMode.REQUIRED)
            LocalDate endDate,
            @RequestParam("pageNumber")
            @Schema(description = "page number", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
            Integer pageNumber,
            @RequestParam("pageSize")
            @Schema(description = "page size", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
            Integer pageSize,
            @RequestParam("baseCurrency")
            @Schema(description = "Base Currency", example = "TWD", requiredMode = Schema.RequiredMode.REQUIRED)
            Currency baseCurrency
    );
}
