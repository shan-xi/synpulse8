package com.synpulse8.ebanking.transactions.controllers;

import com.synpulse8.ebanking.response.dto.ResponseDto;
import com.synpulse8.ebanking.transactions.dto.TransactionListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping(value = "/{accountUid}")
    ResponseEntity<ResponseDto<TransactionListRes>> getTransactionList(
            @PathVariable("accountUid")
            @Schema(description = "User's identity ID", example = "P-0123456789")
            String accountUid,
            @RequestParam("month") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Schema(description = "Transaction month", example = "2023-01-01")
            LocalDate month,
            @RequestParam("pageNumber")
            @Schema(description = "page number", example = "0")
            Integer pageNumber,
            @RequestParam("pageSize")
            @Schema(description = "page size", example = "10")
            Integer pageSize
    );
}
