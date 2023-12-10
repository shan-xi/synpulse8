package com.synpulse8.ebanking.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "transaction list response")
public record TransactionListRes(
        @Schema(description = "page", example = "0")
        Integer pageNumber,
        @Schema(description = "page size", example = "10")
        Integer size,
        @Schema(description = "total pages", example = "20")
        Integer totalPages,
        @Schema(description = "total records", example = "1")
        Long totalElements,
        @Schema(description = "transaction list")
        List<TransactionDto> transactionDtoList) {
}
